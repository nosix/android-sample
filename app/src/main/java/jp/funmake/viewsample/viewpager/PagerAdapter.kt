package jp.funmake.viewsample.viewpager

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan

class PagerAdapter(
        fm: FragmentManager, val data: List<Int>, val icon: Drawable
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = data.size

    // 各ページのフラグメントを生成する
    override fun getItem(position: Int): Fragment {
        return ListFragment().apply {
            arguments = createBundle(data[position])
        }
    }

    // PagerTabStrip のタイトルに使われる
    // https://stackoverflow.com/questions/11839031/android-how-to-add-icons-drawables-to-the-pagertabstrip-from-the-android-suppor
    // http://y-anz-m.blogspot.com/2011/08/androidspannable.html
    override fun getPageTitle(position: Int): CharSequence {
        // サイズを決める
        icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        // テキストの下端に合わせるスパン(テキストを置換する領域)を生成する
        // ImageSpan.ALIGN_BASELINE を指定するとテキストのベースラインに合わされる
        val span = ImageSpan(icon, ImageSpan.ALIGN_BOTTOM)

        return SpannableStringBuilder(" #$position").apply {
            // 1文字目をアイコンに置換する
            setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}
