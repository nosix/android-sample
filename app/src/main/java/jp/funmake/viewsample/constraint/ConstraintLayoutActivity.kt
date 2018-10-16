package jp.funmake.viewsample.constraint

import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.transition.TransitionManager
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import jp.funmake.viewsample.R
import jp.funmake.viewsample.log
import kotlinx.android.synthetic.main.activity_constraint_layout.constraintLayout
import kotlinx.android.synthetic.main.activity_constraint_layout.guideline1
import kotlinx.android.synthetic.main.activity_constraint_layout.item1
import kotlinx.android.synthetic.main.activity_constraint_layout.item2
import kotlinx.android.synthetic.main.activity_constraint_layout.item3
import org.jetbrains.anko.debug

class ConstraintLayoutActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout)

        fun ImageView.initImage() {
            val minHeight = minimumHeight
            setOnClickListener {
                log.debug { "onClick($minimumHeight, $minHeight)" }

                // クリックしたときに、高さを変えてアニメーションさせる
                TransitionManager.beginDelayedTransition(constraintLayout)
                if (minHeight == minimumHeight) {
                    minimumHeight = height * 2
                    // スケールはアニメーションされない
//                    scaleX = 2f
//                    scaleY = 2f
                } else {
                    minimumHeight = minHeight
//                    scaleX = 1f
//                    scaleY = 1f
                }
            }
        }

        item1.initImage()
        item2.initImage()
        item3.initImage()

        // guideline1 のアニメーション
        // constraintLayout の右端から、レイアウト XML で指定した位置に移動する
        val animateToInitGuideline1 = {
            val layoutParams = guideline1.layoutParams as ConstraintLayout.LayoutParams
            log.debug { "${constraintLayout.measuredWidth}, ${layoutParams.guideBegin}" }
            ObjectAnimator.ofInt(
                    guideline1, "GuidelineBegin",
                    constraintLayout.measuredWidth, layoutParams.guideBegin
            ).run {
                duration = 2000
                interpolator = BounceInterpolator()
                start()
            }
        }

        // measureWidth を使うために、レイアウト後にアニメーションを開始する
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            // ラムダを使うと removeOnGlobalLayoutListener が実行できない...
            override fun onGlobalLayout() {
                // 実行は 1 回だけなので、リスナーを削除する
                constraintLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // アニメーションを開始する
                animateToInitGuideline1()
            }
        }
        constraintLayout.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
}
