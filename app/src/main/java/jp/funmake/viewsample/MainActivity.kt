package jp.funmake.viewsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jp.funmake.viewsample.constraint.ConstraintLayoutActivity
import jp.funmake.viewsample.drawer.DrawerActivity
import jp.funmake.viewsample.recycler.RecyclerViewActivity
import jp.funmake.viewsample.swipemenu.SwipeMenuActivity
import jp.funmake.viewsample.viewpager.ViewPagerActivity
import kotlinx.android.synthetic.main.activity_main.button1
import kotlinx.android.synthetic.main.activity_main.button2
import kotlinx.android.synthetic.main.activity_main.button3
import kotlinx.android.synthetic.main.activity_main.button4
import kotlinx.android.synthetic.main.activity_main.button5
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.run {
            text = "RecyclerView"
            setOnClickListener {
                startActivity<RecyclerViewActivity>()
            }
        }

        button2.run {
            text = "ViewPager"
            setOnClickListener {
                startActivity<ViewPagerActivity>()
            }
        }

        button3.run {
            text = "Drawer"
            setOnClickListener {
                startActivity<DrawerActivity>()
            }
        }

        button4.run {
            text = "ConstraintLayout"
            setOnClickListener {
                startActivity<ConstraintLayoutActivity>()
            }
        }

        button5.run {
            text = "Swipe Menu"
            setOnClickListener {
                startActivity<SwipeMenuActivity>()
            }
        }
    }
}
