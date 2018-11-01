package jp.funmake.viewsample.swipemenu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import jp.funmake.viewsample.R

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

    inner class ViewHolder(val container: ViewGroup) : LayeredViewHolder(container) {

        override val foreground: View by container.findLazily(R.id.foreground)
        override val background: View by container.findLazily(R.id.background)

        // foreground
        val textView: TextView by container.findLazily(R.id.textView)

        // background
        val buttonDelete: ImageButton by container.findLazily(R.id.buttonDelete)

        fun bind(position: Int, text: String) {
            textView.text = text
            buttonDelete.setOnClickListener {
                list.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
}

