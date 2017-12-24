package id.asmith.someapp.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.asmith.someapp.R
import id.asmith.someapp.data.local.SQLHandler
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import id.asmith.someapp.data.remote.ApiClient as mApiService


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userDetail = SQLHandler(this).getUser()
        val uid = userDetail["uid"]
        val user = userDetail["username"]
        val name = userDetail["name"]
        val email = userDetail["email"]
        val token = userDetail["token"]
        val text1 = "id : $uid, username: $user, nama : $name, email : $email"
        val text2 = "token anda $token"
        textUid.text =  text1
        textToken.text = text2


        button.setOnClickListener{
            SQLHandler(this).loggedStatusOut(uid!!)
            startActivity<MainActivity>()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val userDetail = SQLHandler(this).getUser()
        val logged = userDetail["logged"]!!.toBoolean()
        if (!logged) {
            startActivity<AuthActivity>()
            finish()
        }
    }


}
