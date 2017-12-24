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

        //access user local data
        val userData = SQLHandler(this).getUserData()
        val uid = userData["uid"]
        val name = userData["name"]
        val token = userData["token"]
        val text1 = "Welcome  $name"
        val text2 = "Your access token $token"

        textUid.text =  text1
        textToken.text = text2

        //Logout button to destroy user session
        button.setOnClickListener{
            SQLHandler(this).loggedUserOut(uid!!)
            startActivity<MainActivity>()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        //On activity resume cek user session are available or not
        //if user session is empty or has been destroy before
        //user will redirect to auth activity
        val userData = SQLHandler(this).getUserData()
        val loggedStatus = userData["logged"]!!.toBoolean()
        if (!loggedStatus) {
            startActivity<AuthActivity>()
            finish()
        }
    }


}
