package jp.funmake.viewsample.swipemenu

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

inline fun <reified T : View> ViewGroup.inflate(@LayoutRes resource: Int): T =
        LayoutInflater.from(context).inflate(resource, this, false) as T
