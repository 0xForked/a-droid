package id.asmith.someapp.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.asmith.someapp.R
import id.asmith.someapp.data.local.db.SQLHandler
import kotlinx.android.synthetic.main.activity_main.*
import id.asmith.someapp.data.remote.ApiClient as mApiService


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = SQLHandler(this)
        val userDetail: HashMap<String, String> = db.getUser()
        val uid = userDetail["uid"]
        val nane = userDetail["name"]
        val email = userDetail["email"]
        val token = userDetail["token"]
        textUid.text =  "id : $uid, nama : $nane, email : $email"
        textToken.text = token
    }


}
