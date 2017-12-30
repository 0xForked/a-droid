package id.asmith.someapp.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty




/**
 * Created by Agus Adhi Sumitro on 30/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */
abstract class PrefHandler {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        /**
         * Initialize PrefDelegate with a Context reference.
         *
         * **This method needs to be called before any other usage of PrefDelegate!!**
         */

        fun init(context: Context) {
            this.context = context
        }

    }

    private val prefs: SharedPreferences by lazy {
        if (context != null)
            context!!.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)
        else
            throw IllegalStateException("Context was not initialized. Call Preferences.init(context) before using it")
    }

    private val listeners = mutableListOf<SharedPrefsListener>()

    abstract class PrefDelegate<T>(val prefKey: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    interface SharedPrefsListener {
        fun onSharedPrefChanged(property: KProperty<*>)
    }

    fun addListener(sharedPrefsListener: SharedPrefsListener) {
        listeners.add(sharedPrefsListener)
    }

    fun removeListener(sharedPrefsListener: SharedPrefsListener) {
        listeners.remove(sharedPrefsListener)
    }

    fun clearListeners() = listeners.clear()

    fun userLogPref(prefKey: String? = null, defaultValue: Boolean = false) = BooleanPrefDelegate(prefKey, defaultValue)

    inner class BooleanPrefDelegate(prefKey: String? = null, private val defaultValue: Boolean) : PrefDelegate<Boolean>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getBoolean(prefKey ?: property.name, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            prefs.edit().putBoolean(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    private fun onPrefChanged(property: KProperty<*>) {
        listeners.forEach { it.onSharedPrefChanged(property) }

    }

    /**
     * Helper method to retrieve a boolean value from [SharedPreferences].
     * @param context a [Context] object.
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */

   /* fun getBooleanPreference(context: Context, key: String, defaultValue: Boolean): Boolean {
        var value = defaultValue
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue)
        }
        return value

    }*/

    /**
     * Helper method to write a boolean value to [SharedPreferences].
     * @param context a [Context] object.
     * @return true if the new value was successfully written to persistent storage.
     */

    /*fun setBooleanPreference(context: Context, key: String, value: Boolean): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            return editor.commit()
        }
        return false
    }*/

}