package jp.funmake.viewsample

import android.content.Context
import org.jetbrains.anko.debug
import org.jetbrains.anko.defaultSharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// shared_prefs/jp.funmake.viewsample_preferences.xml に保存される
// $ adb shell
// $ run-as jp.funmake.viewsample
class DefaultSharedPreferences(
        private val key: String? = null,
        private val defaultValue: Int = 0
) : ReadWriteProperty<Context, Int> {

    private fun toKey(thisRef: Context, property: KProperty<*>): String {
        return key ?: "${thisRef.javaClass.name}.${property.name}"
    }

    override fun getValue(thisRef: Context, property: KProperty<*>): Int {
        return thisRef.defaultSharedPreferences.getInt(toKey(thisRef, property), defaultValue)
    }

    override fun setValue(thisRef: Context, property: KProperty<*>, value: Int) {
        log.debug { "setValue(key=${toKey(thisRef, property)}, value=$value)" }
        thisRef.defaultSharedPreferences.edit()
                .putInt(toKey(thisRef, property), value)
                .apply()
    }
}
