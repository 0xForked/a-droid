package id.asmith.someapp.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.asmith.someapp.R
import id.asmith.someapp.data.local.db.SQLHandler
import id.asmith.someapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_auth_signin.*
import okhttp3.ResponseBody
import org.jetbrains.anko.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import id.asmith.someapp.data.remote.ApiClient as mApiService
import id.asmith.someapp.util.CommonUtils as utils

class AuthSigninFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_signin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!utils().isNetworkConnected(context)) {
            activity?.longToast("No internet connection")
        }

        //Button Sign in
        val buttonGo = button_signin_go
        buttonGo.setOnClickListener {

            val email = input_signin_email.text.toString().trim()
            val password = input_signin_password.text.toString().trim()

            if (email.isEmpty()) activity?.longToast( getString(R.string.caption_empty_email))

            if (!utils().isEmailValid(email)) activity?.longToast( getString(R.string.caption_valid_email))

            if (password.isEmpty()) activity?.longToast( getString(R.string.caption_empty_password))

            if (!email.isEmpty() and !password.isEmpty() and utils().isEmailValid(email))
                singIn(email, password)

        }

        //Forgot password caption
        val forgot = text_signin_forgot
        val captionPass = "<b>Forgot password?</b>"

        @Suppress("DEPRECATION")
        val spannableStringBuilderForgot = SpannableStringBuilder(Html.fromHtml(captionPass))
        spannableStringBuilderForgot.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                forgotPassword()
            }
        }, captionPass.indexOf("Forgot password?") - 3,
                spannableStringBuilderForgot.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        @Suppress("DEPRECATION")
        spannableStringBuilderForgot.setSpan(ForegroundColorSpan
        (resources.getColor(R.color.colorPrimary)),
                captionPass.indexOf("Forgot password?") - 3,
                spannableStringBuilderForgot.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        forgot.text = spannableStringBuilderForgot
        forgot.movementMethod = LinkMovementMethod.getInstance()

        //Register caption
        val signUp = text_signin_signup
        val captionRegister = "Don't have an account? <b>Sign up</b>"

        @Suppress("DEPRECATION")
        val spannableStringBuilderRegister = SpannableStringBuilder(Html.fromHtml(captionRegister))
        spannableStringBuilderRegister.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                signUp()
            }
        }, captionRegister.indexOf("Sign up") - 3,
                spannableStringBuilderRegister.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        @Suppress("DEPRECATION")
        spannableStringBuilderRegister.setSpan(ForegroundColorSpan
        (resources.getColor(R.color.colorPrimary)),
                captionRegister.indexOf("Sign up") - 3,
                spannableStringBuilderRegister.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signUp.text = spannableStringBuilderRegister
        signUp.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun singIn(email: String, password: String) {
        val dialog = activity!!.indeterminateProgressDialog("Please wait")
        dialog.setCancelable(false)
        dialog.show()

        val background = object : Thread() {
            override fun run() {

                try {

                    mApiService.authService.signIn(email, password)
                            .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>,
                                                response: Response<ResponseBody>) {

                            if (response.isSuccessful){
                                try {
                                    val thisResult = JSONObject(response.body()!!.string())
                                    val message = thisResult.getString("message")

                                    val thisUserData = thisResult.getJSONObject("user")
                                    val uid = thisUserData.getString("uid")
                                    val token = thisUserData.getString("token")
                                    val accStatus = thisUserData.getString("isActive").toInt()

                                    if (accStatus != 0) {

                                      /*  activity?.alert("$message your id $uid " +
                                                "your token $token", "Welcome") {
                                            yesButton {  activity?.toast("Welcome back") }
                                        }?.show()?.setCancelable(false)
*/
                                        Log.d("Debug", "Message : $message")

                                        localData(uid, token)

                                    } else {

                                        activity?.longToast( "Account inActive")
                                        activity?.alert("Please contact our administrator",
                                                "Account inActive") {
                                            yesButton {  activity?.toast("Whoops") }
                                        }?.show()?.setCancelable(false)

                                    }

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                                dialog.dismiss()

                            } else {
                                try {
                                    val thisResult = JSONObject(response.errorBody()!!.string())
                                    val error = thisResult.getString("error").toBoolean()
                                    val message = thisResult.getString("message")

                                    Log.e("Debug", "notSuccess message : $response " +
                                            "$thisResult")
                                    activity?.alert("Message: $message ",
                                            "Error : $error") {
                                        yesButton {  activity?.toast("Whoops") }
                                    }?.show()?.setCancelable(false)

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                                dialog.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            Log.e("Debug", "OnFailure : $t")
                            dialog.dismiss()
                        }

                    })

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // start thread
        background.start()
    }

    private fun localData(uid: String?, token: String?) {
        val dialog = activity!!.indeterminateProgressDialog("Updating local data")
        dialog.setCancelable(false)
        dialog.show()

        try {

            if (uid != null && token != null) {
                mApiService.userService.userDetail(uid, token).enqueue(object :
                        Callback<ResponseBody> {

                    override fun onResponse(call: Call<ResponseBody>?,
                                            response: Response<ResponseBody>) {

                        if (response.isSuccessful) {
                            val thisResults = JSONObject(response.body()!!.string())
                            val thisUserData = thisResults.getJSONObject("user")
                            val userId = thisUserData.getString("uid")
                            val username = thisUserData.getString("username")
                            val name = thisUserData.getString("name")
                            val userEmail = thisUserData.getString("email")
                            val phone = thisUserData.getString("phone")
                            val userToken = thisUserData.getString("token")
                            val isLogged = "true"
                            val created = Calendar.getInstance().time
                            val updated = Calendar.getInstance().time

                            val database = SQLHandler(context)
                            database.addUserData(userId, username, name, userEmail, phone,
                                    userToken, isLogged, created.toString(), updated.toString())

                            activity!!.startActivity<MainActivity>(
                                    "EXTRA_UID" to uid, "EXTRA_TOKEN" to token)

                            activity!!.finish()



                            dialog.dismiss()
                        } else {
                            activity!!.longToast("Failed")
                            dialog.dismiss()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.e("Debug", "OnFailure : $t")
                        dialog.dismiss()
                    }

                })
            } else {
                activity!!.longToast("Token and uid is null")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Forgot password in action .remove(this)
    private fun forgotPassword() {
        activity?.toast( "Forgot password")
        fragmentManager!!
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, AuthForgotFragment())
                .addToBackStack(null)
                .commit()

    }

    //Sign up in action .remove(this)
    private fun signUp() {
        activity?.toast("Register")
        fragmentManager!!
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, AuthSignupFragment())
                .addToBackStack(null)
                .commit()

    }

}