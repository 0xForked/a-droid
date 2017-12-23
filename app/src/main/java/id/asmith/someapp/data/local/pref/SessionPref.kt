package id.asmith.someapp.data.local.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import id.asmith.someapp.util.AppConstants as utils

/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */

class SessionPref {

    private val prefTag = SessionPref::class.java.simpleName
    private var prefs: SharedPreferences? = null
    private var editor: Editor? = null


    @SuppressLint("CommitPrefEdits")
    fun SessionPref(context: Context) {
        prefs = context.getSharedPreferences(prefTag, Context.MODE_PRIVATE)
    }

    fun getCurrentUID(): Long? {
        val userId = prefs?.getLong(utils.PREF_KEY_CURRENT_USER_ID, utils.NULL_INDEX)
        return if (userId == id.asmith.someapp.util.AppConstants.NULL_INDEX) null else userId
    }

    @SuppressLint("CommitPrefEdits")
    fun setCurrentUID(userId: Long?) {
        val id = userId ?: utils.NULL_INDEX
        prefs?.edit()?.putLong(utils.PREF_KEY_CURRENT_USER_ID, id)?.apply()

    }

    fun getAccessToken(): String? {
        return prefs?.getString(utils.PREF_KEY_CURRENT_USER_TOKEN, null)
    }


    fun setAccessToken(accessToken: String) {
        prefs?.edit()?.putString(utils.PREF_KEY_CURRENT_USER_TOKEN, accessToken)?.apply()

    }

    fun setLogin(isLoggedIn: Boolean) {
        editor?.putBoolean(utils.PREF_KEY_USER_LOGGED_IN_MODE, isLoggedIn)
        editor?.commit()
    }

    fun isLoggedIn(): Boolean {
        return prefs!!.getBoolean(utils.PREF_KEY_USER_LOGGED_IN_MODE, false)
    }

}