// see: Create a List with RecyclerView
//   https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=ja

package jp.funmake.viewsample.recycler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import jp.funmake.viewsample.R
import kotlinx.android.synthetic.main.activity_recycler_view.recycler

class RecyclerViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val items = List(100) { Item(it.toString()) }

        recycler.run {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerViewAdapter(items)
        }
    }
}
