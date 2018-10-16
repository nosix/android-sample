// see: ナビゲーションドロワーの作成
//   https://developer.android.com/training/implementing-navigation/nav-drawer?hl=ja

package jp.funmake.viewsample.drawer

import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import jp.funmake.viewsample.R
import jp.funmake.viewsample.log
import kotlinx.android.synthetic.main.activity_drawer.drawer
import kotlinx.android.synthetic.main.activity_drawer.drawerLayout
import kotlinx.android.synthetic.main.activity_drawer.toolbar
import org.jetbrains.anko.debug

class DrawerActivity : AppCompatActivity() {

    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        // supportActionBar に Toolbar を設定する
        setSupportActionBar(toolbar)

        val drawerItems = Array(20) { "Menu Item $it" }

        drawer.run {
            adapter = ArrayAdapter<String>(context, R.layout.drawer_row, drawerItems)
            setOnItemClickListener { _, _, position, _ ->
                log.debug { "onItemClick($position)" }
                drawerLayout.closeDrawers() // ドロワーを閉じる
            }
        }

        mToggle = object : ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                log.debug { "onDrawerOpened" }
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                log.debug { "onDrawerClosed" }
            }
        }

        drawerLayout.addDrawerListener(mToggle)

        // 注意 actionBar ではない
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true) // 戻る機能としてホームボタンを表示する
            // アプリアイコンをタッチできる様にするらしいが、
            // Toolbar の実装では何も行っていないため、
            // なくても動作する
            //setHomeButtonEnabled(true)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // 状態を同期する（アプリアイコンが変わる）
        mToggle.syncState()
    }

    // 構成の変更を独自に処理する場合には必要
    // https://developer.android.com/guide/topics/resources/runtime-changes?hl=ja
//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        log.debug { "onConfigurationChanged" }
//        super.onConfigurationChanged(newConfig)
//        mToggle.onConfigurationChanged(newConfig)
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // ActionBarDrawerToggle にイベントを渡してドロワーを開閉する
        if (mToggle.onOptionsItemSelected(item)) {
            // ActionBarDrawerToggle で処理されたらメソッドは終了
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
