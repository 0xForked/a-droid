package id.asmith.someapp.ui.fragments


import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import id.asmith.someapp.R
import id.asmith.someapp.data.local.SQLHandler
import id.asmith.someapp.ui.activities.AuthActivity
import id.asmith.someapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_auth_lock_user.*
import okhttp3.ResponseBody
import org.jetbrains.anko.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import id.asmith.someapp.data.remote.ApiClient.authService as mApiService

class AuthLockFragment : Fragment(), PopupMenu.OnMenuItemClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_lock_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //get user local data
        val userData = SQLHandler(activity).getUserData()
        //set user local data if exist
        text_lock_name.text = userData["name"]
        text_lock_email.text = userData["email"]

        //This time i not use the spannable
        //and replace it with paint flag and typeface
        //forgot pwd
        text_lock_forgot.paintFlags = (text_lock_forgot.paintFlags or
                Paint.UNDERLINE_TEXT_FLAG)
        text_lock_forgot.setTypeface(null, Typeface.BOLD)
        text_lock_forgot.setOnClickListener {
            forgotPassword()
        }

        //sign in
        text_lock_singin.paintFlags = (text_lock_forgot.paintFlags or
                Paint.UNDERLINE_TEXT_FLAG)
        text_lock_singin.setTypeface(null, Typeface.BOLD)
        text_lock_singin.setOnClickListener {
            signIn()
        }

        //menu on user data right side
        button_lock_menu.setOnClickListener { view ->
            showMenu(view)
        }

        button_lock_go.setOnClickListener {
            val lockEmail =  text_lock_email.text.toString().trim()
            val lockPassword = input_lock_password.text.toString().trim()

            if (lockPassword.isEmpty()) activity?.longToast( getString(R.string.caption_empty_password))

            if ( !lockPassword.isEmpty()) loggedUserIn(lockEmail, lockPassword)
        }



    }

    private fun loggedUserIn(email: String, password: String) {
        //activity!!.longToast("awww email: $email password: $password")

        //progress dialog with anko
        val dialog = activity!!.indeterminateProgressDialog("Please wait")
        dialog.setCancelable(false)
        dialog.show()

        val background = object : Thread() {
            override fun run() {

                try {

                    mApiService.signIn(email, password).enqueue(object : Callback<ResponseBody> {
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

                                        Log.d("Debug", "Message : $message $uid $token")

                                        SQLHandler(activity).loggedUserIn(uid, token)
                                        activity?.startActivity<MainActivity>()
                                        activity?.finish()

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

    //Show menu
    private fun showMenu(view: View) {
        val popup = PopupMenu(activity, view)
        popup.setOnMenuItemClickListener(this@AuthLockFragment)
        popup.inflate(R.menu.menu_lock)
        popup.show()
    }

    //forgot password fragment
    private fun forgotPassword() {
        fragmentManager!!
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, AuthForgotFragment())
                .addToBackStack(null)
                .commit()
    }

    //Sign in fragment
    private fun signIn() {
        fragmentManager!!
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, AuthSigninFragment())
                .addToBackStack(null)
                .commit()
    }

    //On menu action clicked
    override fun onMenuItemClick(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.action_delete ->
                activity?.alert("Are you sure? ",
                        "Remove account") {
                    yesButton {
                        SQLHandler(activity).deleteUserData()
                        activity?.finish()
                        activity?.startActivity<AuthActivity>()
                        activity?.toast("Whoops")
                    }
                    noButton {}
                }?.show()?.setCancelable(false)

        }

        return true
    }

}
