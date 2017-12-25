package id.asmith.someapp.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
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
import id.asmith.someapp.data.local.SQLHandler
import id.asmith.someapp.data.model.LocalUser
import id.asmith.someapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_auth_signup.*
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

class AuthSignupFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_signup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Login caption
        val captionLogin = "Already a member? <b>Sign in</b>"

        @Suppress("DEPRECATION")
        val spannableStringBuilderLogin = SpannableStringBuilder(Html.fromHtml(captionLogin))
        spannableStringBuilderLogin.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                signIn()
            }
        }, captionLogin.indexOf("Sign in") - 3,
                spannableStringBuilderLogin.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        @Suppress("DEPRECATION")
        spannableStringBuilderLogin.setSpan(ForegroundColorSpan
        (resources.getColor(R.color.colorPrimary)),
                captionLogin.indexOf("Sign in") - 3,
                spannableStringBuilderLogin.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text_signup_signin.text = spannableStringBuilderLogin
        text_signup_signin.movementMethod = LinkMovementMethod.getInstance()

        /* button go  in this method before the app go to main function,
         * this method will check the valid input that user give
         * in this case this app will need input like common register form
         * user full name, username, email, phone, and password
         */
        button_signup_go.setOnClickListener {
            val regName = input_signup_fullName.text.toString().trim()
            val regUsername = "Asmith_Users" //actually we not need this
            val regEmail = input_signup_email.text.toString().trim()
            val regPhone = input_signup_phone.text.toString().trim()
            val regPassword = input_signup_password.text.toString().trim()

            if (regName.isEmpty()) activity?.longToast(getString(R.string.caption_empty_name))
            if (regEmail.isEmpty()) activity?.longToast(getString(R.string.caption_empty_email))
            if (regPhone.isEmpty()) activity?.longToast(getString(R.string.caption_empty_phone))
            if (regPassword.isEmpty()) activity?.longToast(getString(R.string.caption_empty_password))

            if (!utils().isEmailValid(regEmail)) activity?.longToast( getString(R.string.caption_valid_email))
            if (!utils().isPhoneValid(regPhone)) activity?.longToast( getString(R.string.caption_valid_phone))

            if (!regName.isEmpty() and !regEmail.isEmpty() and utils().isEmailValid(regEmail)
                    and !regPhone.isEmpty() and utils().isPhoneValid(regPhone)
                    and !regPassword.isEmpty()) {

                singUp(regName, regUsername, regPhone, regEmail, regPassword)

            }
        }

        //maybe some day #lol
        image_signup_userPic.setOnClickListener {
            activity?.longToast("Add user pic")
        }

    }

    /*user registration main function
    * after app check the validation input from user button go will pass the data in
    * this function in this function app will send a request to server side app (API service)
    * if success server app will give a response like json object after that app will
    * save user basic information in local databases
    * */
    private fun singUp(regName: String, regUsername: String, regPhone: String,
                       regEmail: String, regPassword: String ) {

        val dialog = activity!!.indeterminateProgressDialog("Please wait")
        dialog.setCancelable(false)
        dialog.show()

        val background = object : Thread() {
            override fun run() {

                try {

                    mApiService.authService.signUp(regName, regUsername, regPhone,
                            regEmail, regPassword).enqueue(object : Callback<ResponseBody>{
                        override fun onResponse(call: Call<ResponseBody>?,
                                                        response: Response<ResponseBody>) {
                            if (response.isSuccessful){
                                try {
                                    val thisResult = JSONObject(response.body()!!.string())
                                    val thisUserData = thisResult.getJSONObject("user")

                                    val userId = thisUserData.getString("uid")
                                    val userToken = thisUserData.getString("token")
                                    val userInitial = thisUserData.getString("username")
                                    val userFullName = thisUserData.getString("name")
                                    val userEmail = thisUserData.getString("email")
                                    val userPhone = thisUserData.getString("phone")
                                    val isLogged = "true"
                                    val isCreated = Calendar.getInstance().time
                                    val isUpdated = Calendar.getInstance().time


                                    val database = SQLHandler(context)
                                    database.addUserData(LocalUser(userId, userInitial,
                                            userFullName, userEmail, userPhone, userToken,
                                            isLogged, isCreated.toString(), isUpdated.toString()))

                                    activity!!.startActivity<MainActivity>()

                                    activity!!.finish()

                                    dialog.dismiss()

                                    //Log.e("test", "$userFullName $userPhone")
                                    /*activity?.alert("Register $message your id $uid " +
                                            "your token $token", "Welcome") {
                                        yesButton {  activity?.toast("Welcome") }
                                    }?.show()?.setCancelable(false)*/

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                                dialog.dismiss()

                            } else {
                                try {
                                    val thisResult = JSONObject(response.errorBody()!!.string())

                                    Log.e("Debug", "notSuccess : $response " +
                                            "$thisResult")

                                    activity?.alert("Message: $thisResult ",
                                            "Error : true") {
                                        yesButton {  activity?.toast("Whoops") }
                                    }?.show()?.setCancelable(false)

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                                dialog.dismiss()

                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            dialog.dismiss()
                            activity?.longToast("Can't connect with server app")
                            Log.e("Debug", "OnFailure : $t")
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


    //Back to sign in fragment and destroy this fragment
    private fun signIn() {
        activity?.toast("Back")
        fragmentManager?.popBackStack()

    }

}
