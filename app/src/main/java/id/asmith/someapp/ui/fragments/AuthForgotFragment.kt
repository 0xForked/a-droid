package id.asmith.someapp.ui.fragments


import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.asmith.someapp.R
import kotlinx.android.synthetic.main.fragment_auth_forgot_password.*
import okhttp3.ResponseBody
import org.jetbrains.anko.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import id.asmith.someapp.data.remote.ApiClient as mApiService
import id.asmith.someapp.util.CommonUtils as utils

class AuthForgotFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_forgot_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //aahhh bring me back
        text_forgot_back.paintFlags = (text_forgot_back.paintFlags or
                Paint.UNDERLINE_TEXT_FLAG)
        text_forgot_back.setTypeface(null, Typeface.BOLD)
        text_forgot_back.setOnClickListener {
            onBack()
        }

        //Send me an email :v before that please check if my input is wrong or not
        text_forgot_submit.paintFlags = (text_forgot_submit.paintFlags or
                Paint.UNDERLINE_TEXT_FLAG)
        text_forgot_submit.setTypeface(null, Typeface.BOLD)
        text_forgot_submit.setOnClickListener {
            val forgotEmail =  input_forgot_email.text.toString().trim()

            if (forgotEmail.isEmpty()) activity?.longToast( getString(R.string.caption_empty_email))
            if (!utils().isEmailValid(forgotEmail)) activity?.longToast( getString(R.string.caption_valid_email))
            if (!forgotEmail.isEmpty() and utils().isEmailValid(forgotEmail))
                forgotPassword(forgotEmail)
        }
    }

    //This is the code that will send a messages to your email ggwp guys
    private fun forgotPassword(forgotEmail: String) {
        //progress dialog with anko
        val dialog = activity!!.indeterminateProgressDialog("Please wait")
        dialog.setCancelable(false)
        dialog.show()

        val background = object : Thread() {
            override fun run() {
                try {
                    mApiService.userService.forgotPassword(forgotEmail)
                            .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>,
                                                response: Response<ResponseBody>) {

                            if (response.isSuccessful) {

                                activity?.alert("Mail has been sent to your address",
                                            "Success") {
                                        yesButton {  onBack() }
                                }?.show()?.setCancelable(false)


                            } else {

                                activity!!.longToast("Cant find your email in database")

                            }

                            dialog.dismiss()
                        }

                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            Log.e("Debug", "OnFailure : $t")
                            dialog.dismiss()
                        }
                    })

                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }
        }

        // start thread
        background.start()

    }

    private fun onBack() {
        activity?.toast("Back")
        fragmentManager?.popBackStack()

    }
}
