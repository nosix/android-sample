package jp.funmake.viewsample.swipemenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import jp.funmake.viewsample.R
import jp.funmake.viewsample.log
import kotlinx.android.synthetic.main.activity_swipe_menu.content
import org.jetbrains.anko.info

class SwipeMenuActivity : AppCompatActivity() {

    private val mAdapter = RecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_menu)

        content.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            ItemMotionHandler.newInstance().let {
                it.itemTouchHelper.attachToRecyclerView(this)
                it.onDrop = { fromPos, toPos ->
                    log.info { "onDrag($fromPos, $toPos)" }
                    val item = mAdapter.list.removeAt(fromPos)
                    mAdapter.list.add(toPos, item)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

        mAdapter.list = MutableList(100) { "Item $it" }
    }
}
