package jp.funmake.viewsample.recycler

import android.annotation.TargetApi
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.funmake.viewsample.R
import jp.funmake.viewsample.log
import kotlinx.android.synthetic.main.recycler_row_base.view.itemText
import org.jetbrains.anko.debug

class RecyclerViewAdapter(val items: List<Item>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // parent は RecyclerView
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_row_base, parent, false) as ConstraintLayout
        // attachToRoot = true だと
        //   view は RecyclerView
        //   root 引数は必須
        // attachToRoot = false だと
        //   view は recycler_row の root
        //   root 引数を指定すると root の LayoutParameter が設定される
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].let { item ->
            holder.bind(item) {
                // onClickListener
                item.isSelected = !item.isSelected
                notifyItemChanged(position)
            }
        }
    }

    class ViewHolder(val container: ConstraintLayout) : RecyclerView.ViewHolder(container) {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        fun bind(item: Item, onClickListener: (View) -> Unit) {
            log.debug( "bind($item)")
            itemView.run {
                itemText.text = item.title
                container.setOnClickListener(onClickListener)
                val layout =
                        if (item.isSelected)
                            R.layout.recycler_row_del
                        else
                            R.layout.recycler_row_base
                log.debug { container.isLaidOut }
                ConstraintSet().run {
                    // 制約の集合に、レイアウトリソースで設定された制約を複製する
                    clone(container.context, layout)
                    // アニメーションをカスタマイズする場合に使うが
                    // container.isLaidOut が true にならないため
                    // カスタマイズが有効にならない
                    //TransitionManager.beginDelayedTransition(container, ChangeBounds())

                    // 制約を適用すると自動的にアニメーションする
                    applyTo(container)
                }
            }
        }
    }
}
