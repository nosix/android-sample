package jp.funmake.viewsample.swipemenu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import jp.funmake.viewsample.R
import kotlinx.android.synthetic.main.swipemenu_row.view.buttonDelete
import kotlinx.android.synthetic.main.swipemenu_row.view.textView
import kotlinx.android.synthetic.main.swipemenu_row.view.foreground as foregroundPane
import kotlinx.android.synthetic.main.swipemenu_row.view.background as backgroundPane

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var list: MutableList<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ViewGroup に拡張関数 inflate を追加している
        return ViewHolder(parent.inflate(R.layout.swipemenu_row))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, list[position])
    }

    inner class ViewHolder(view: View) : LayeredViewHolder(view) {

        override val foreground: View = itemView.foregroundPane
        override val background: View = itemView.backgroundPane

        fun bind(position: Int, text: String) {
            itemView.run {
                textView.text = text
                buttonDelete.setOnClickListener {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }
}

