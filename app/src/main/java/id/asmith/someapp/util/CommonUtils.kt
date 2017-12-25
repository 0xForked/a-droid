package id.asmith.someapp.util

import android.content.Context
import android.net.ConnectivityManager
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */

class CommonUtils {

    //Cek valid email
    fun isEmailValid(email: String): Boolean {

        val pattern: Pattern = Pattern.compile(AppConstants.EMAIL_PATTERN)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()

    }

    //cek phone validation
    fun isPhoneValid(phone: String): Boolean {
        val pattern: Pattern = Pattern.compile(AppConstants.PHONE_PATTERN)
        val matcher: Matcher = pattern.matcher(phone)
        return matcher.matches()
    }

    //cek username validation
    fun inUsernameValid (username: String): Boolean {
        val pattern: Pattern = Pattern.compile(AppConstants.USERNAME_PATTERN)
        val matcher: Matcher = pattern.matcher(username)
        return matcher.matches()
    }

    //Cek connection
    fun isNetworkConnected(context: Context?): Boolean {

        //Connection Manager
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting

    }


}