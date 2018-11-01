package jp.funmake.viewsample.swipemenu

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * ドロップされたときに呼び出される
 */
typealias OnDropListener = (fromPos: Int, toPos: Int) -> Unit

abstract class LayeredViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract val foreground: View
    abstract val background: View
}

class ItemMotionHandler private constructor() : ItemTouchHelper.Callback() {

    companion object {
        fun newInstance(): ItemMotionHandler =
            ItemMotionHandler().apply {
                mItemTouchHelper = ItemTouchHelper(this)
            }
    }

    /**
     * ドロップしたときに行う処理
     *
     * 設定しない場合は、ドラッグ操作を受け付けない．
     */
    var onDrop: OnDropListener? = null

    /**
     * 関連づけられた ItemTouchHelper
     *
     * ItemTouchHelper に attach する RecyclerView では、ViewHolder が LayeredViewHolder でなければならない．
     */
    val itemTouchHelper: ItemTouchHelper
        get() = mItemTouchHelper

    // RecoverAnimation を削除するために必要
    private lateinit var mItemTouchHelper: ItemTouchHelper

    // Swipe Menu が表示されている Item の foreground
    private var mLockedForeground: View? = null

    // Drag の元と先
    private var mMoveFrom: Int? = null
    private var mMoveTo: Int? = null

    // RecyclerView に変更があった場合に固定を解除する
    private val mUnlockForeground = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            // 複数のリスナーが登録されないようにする
            view?.removeOnLayoutChangeListener(this)
            // 固定されている場合は解除する
            mLockedForeground?.let {
                getDefaultUIUtil().clearView(it)
                mLockedForeground = null
            }
        }
    }

    private fun RecyclerView.ViewHolder.cast() = this as LayeredViewHolder

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 上下方向のDragと右方向のSwipeを有効にする
        return makeMovementFlags(
            if (onDrop != null) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0,
            ItemTouchHelper.END or ItemTouchHelper.START
        )
    }

    // メニューが半分開いたらSwipeにする
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val holder = viewHolder.cast()
        return holder.background.width.toFloat() / holder.itemView.width / 2
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // actionState:
        //   0 - 選択が解除された
        //   1 - 横移動が開始された
        //   2 - 縦移動が開始された

        when (actionState) {
            2 -> {
                // 縦移動が開始したら、開始位置を記録する
                viewHolder?.cast()?.also { holder ->
                    mMoveFrom = holder.adapterPosition
                }
            }
            0 -> {
                // viewHolder == null
                // 縦移動が終了したら、コールバックを実行する
                mMoveFrom?.also { fromPos ->
                    mMoveTo?.also { toPos ->
                        onDrop?.invoke(fromPos, toPos)
                    }
                }
                mMoveFrom = null
                mMoveTo = null
            }
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onMove(
        recyclerView: RecyclerView, dragged: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean {
        // 1つ移動する度に実行される
        mMoveTo = target.adapterPosition

        // Dragで移動したときにItemを入れ替える
        recyclerView.adapter?.notifyItemMoved(dragged.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
        val foreground = holder.cast().foreground

        // 右方向にSwipeした場合
        if (direction == ItemTouchHelper.END) {
            // Itemを固定する
            mLockedForeground = foreground
            foreground.addOnLayoutChangeListener(mUnlockForeground)
        }

        // 左方向にSwipeした場合
        if (direction == ItemTouchHelper.START) {
            // 固定されている場合は解除する
            if (foreground == mLockedForeground) {
                mLockedForeground = null
            }
        }

        // RecoverAnimation を削除する(notifyItemRemovedの代わり)
        // onChildDrawのコメントの内容を解消するために必要
        ::mItemTouchHelper.isInitialized || throw AssertionError("ItemTouchHelper must be attached.")
        mItemTouchHelper.onChildViewDetachedFromWindow(holder.itemView)
    }

    // Itemの描画を行う
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // isCurrentlyActive は、RecoverAnimation による移動では false、selected ViewHolder の移動 では true
        // Swipe が発生するときは ItemTouchHelper::mIsPendingCleanup が true になり、RecoverAnimation が remove されない
        // mPendingCleanup に ViewHolder の itemView が登録され、同じ Item を次に移動する時に remove される
        // もしくは ItemTouchHelper::onChildViewDetachedFromWindow が呼ばれた時に remove される
        // RecoverAnimation が remove されないことにより、isCurrentlyActive == false での呼び出しが発生する
        //
        // Item を左にスライドさせる場合、
        // Swipe されていない Item では dX が負値になり、右に Swipe された Item では dX が最大値から減少していく(負値にならない)
        // 指を離すと dX は増大する(指を左に動かしても、右方向の移動になる)
        // mDx = x - mInitialTouchX の正負によって swipeDir を決めている
        // mInitialTouchX -= animation.mX であり、mInitialTouchX が負の値であるため mDx が正となり右方向になる
        // animation.mX は RecoverAnimation が終了した段階で右端の x 座標になっている
        // RecoverAnimation が remove されないことにより、左にスライドさせた場合でも右に移動する

        val holder = viewHolder.cast()

        when {
            dX == 0f -> { // Drag(移動) or 横移動の終端
                // itemViewを動かす
                getDefaultUIUtil().onDraw(c, recyclerView, holder.itemView, dX, dY, actionState, isCurrentlyActive)

                // 縦移動の場合は、固定を解除する
                if (dY != 0f) {
                    unlockForeground(holder, isForce = true)
                }
            }
            dX > 0f -> { // Swipe(メニュー表示)
                // 固定している場合は何もしない
                if (holder.foreground == mLockedForeground) return

                // backgroundの幅までしか開かない
                val maxWidth = holder.background.width.toFloat()
                val x = min(dX, maxWidth)

                // backgroundを固定して、foregroundだけを動かす
                getDefaultUIUtil().onDraw(c, recyclerView, holder.background, 0f, 0f, actionState, isCurrentlyActive)
                getDefaultUIUtil().onDraw(c, recyclerView, holder.foreground, x, dY, actionState, isCurrentlyActive)

                unlockForeground(holder)
            }
            dX < 0f -> { // Swipe(元に戻す)
                // 固定していない場合は何もしない
                if (holder.foreground != mLockedForeground) return

                // backgroundの幅の間でのみ動く
                val maxWidth = holder.background.width.toFloat()
                val x = max(min(dX + maxWidth, maxWidth), 0f)

                // backgroundを固定して、foregroundだけを動かす
                getDefaultUIUtil().onDraw(c, recyclerView, holder.background, 0f, 0f, actionState, isCurrentlyActive)
                getDefaultUIUtil().onDraw(c, recyclerView, holder.foreground, x, dY, actionState, isCurrentlyActive)

                unlockForeground(holder)
            }
        }
    }

    private fun unlockForeground(selected: LayeredViewHolder, isForce: Boolean = false) {
        // 固定を入れ替えるために、固定済みのItemを解除する
        mLockedForeground?.let {
            if (it != selected.foreground || isForce) {
                getDefaultUIUtil().clearView(it)
                mLockedForeground = null
            }
        }
    }
}
