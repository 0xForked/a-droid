package id.asmith.someapp.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.asmith.someapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //private val uid: String? = intent.getStringExtra("EXTRA_UID")
    //val token = intent!!.getStringExtra("USER_TOKEN")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uid = intent.getStringExtra("EXTRA_UID") ?:
                throw IllegalArgumentException("Must pass EXTRA_POST_KEY")

        val token = intent.getStringExtra("EXTRA_TOKEN") ?:
                throw IllegalArgumentException("Must pass EXTRA_POST_KEY")

        textUid.text = uid
        textToken.text = token
    }
}
