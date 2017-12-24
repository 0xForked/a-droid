package id.asmith.someapp.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import id.asmith.someapp.R
import id.asmith.someapp.data.local.db.SQLHandler
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background = object : Thread() {
            override fun run() {
                try {

                    // Thread will sleep for 5 seconds
                    sleep((5 * 1000).toLong())

                    val userDetail = SQLHandler(baseContext).getUser()
                    val isLoggedIn = userDetail["logged"]!!.toBoolean()

                    if (isLoggedIn){
                        // Main Activity
                        openMainActivity()
                    } else {
                        // Auth Activity
                        openAuthActivity()
                    }

                    Log.e("Logged status", isLoggedIn.toString())

                } catch (e: Exception) {

                    e.printStackTrace()

                }
            }
        }

        // start thread
        background.start()

    }

    fun openAuthActivity() {

        // After 5 seconds redirect to another intent
        startActivity<AuthActivity>()

        //Remove activity
        finish()

    }

    fun openMainActivity() {

        // After 5 seconds redirect to another intent
        startActivity<MainActivity>()

        //Remove activity
        finish()

    }
}