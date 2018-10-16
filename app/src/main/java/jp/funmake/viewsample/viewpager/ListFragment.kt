package jp.funmake.viewsample.viewpager

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.funmake.viewsample.R
import jp.funmake.viewsample.log
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class ListFragment : Fragment() {

    companion object {
        private const val ARG_SIZE = "arg_size"
    }

    fun createBundle(size: Int): Bundle = Bundle().apply {
        putInt(ARG_SIZE, size)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // container は ViewPager
        val viewGroup = inflater.inflate(R.layout.linear, container, false) as ViewGroup
        // container の LayoutParameter を設定するが、root は container ではなく inflate された View
        val size = arguments?.getInt(ARG_SIZE, 0) ?: 0
        fun Int.format(): String = String.format("%02d", this)
        repeat(size) {
            viewGroup.addView(TextView(context).apply {
                text = "Item${it.format()}"
            })
        }
        return viewGroup
    }
}
