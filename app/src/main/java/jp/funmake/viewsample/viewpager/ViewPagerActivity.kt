// see: Creating swipe views with tabs
//   https://developer.android.com/training/implementing-navigation/lateral?hl=ja
//   Add Tabs to the Action Bar の内容は Deprecated なので無視する

package jp.funmake.viewsample.viewpager

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import jp.funmake.viewsample.DefaultSharedPreferences
import jp.funmake.viewsample.R
import jp.funmake.viewsample.log
import kotlinx.android.synthetic.main.activity_view_pager.pager
import kotlinx.android.synthetic.main.activity_view_pager.pagerTabStrip
import org.jetbrains.anko.debug

class ViewPagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        // タブで使うアイコン（アイコンが必要な場合のみ）
        val icon = ResourcesCompat.getDrawable(
                resources, android.R.drawable.ic_menu_search, theme) ?: throw AssertionError()

        pager.adapter = PagerAdapter(supportFragmentManager, listOf(1, 2, 3), icon)

        // タブの設定
        pagerTabStrip.run {
            setNonPrimaryAlpha(0.5f) // 非表示のページタイトルの透過度
            textSpacing *= 3 // ページタイトルの間隔
            setTabIndicatorColorResource(R.color.colorAccent) // インジケータの色
            drawFullUnderline = true // 下線の表示
        }
    }

    // アクティビティが変わる時に、現在のページを保持する
    private var savedCurrentPage: Int by DefaultSharedPreferences()

    override fun onResume() {
        super.onResume()
        pager.currentItem = savedCurrentPage
    }

    override fun onPause() {
        super.onPause()
        log.debug { "pause: ${pager.currentItem}" }
        savedCurrentPage = pager.currentItem
    }
}
