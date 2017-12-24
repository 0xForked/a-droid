package id.asmith.someapp.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.asmith.someapp.R
import id.asmith.someapp.data.local.SQLHandler
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background = object : Thread() {
            override fun run() {
                try {

                    // Thread will sleep for 3 seconds
                    sleep((3 * 1000).toLong())

                    val userDetail = SQLHandler(baseContext).getUser()
                    val isLoggedIn = userDetail["logged"]

                    if (isLoggedIn != null && isLoggedIn.toBoolean()) {

                        // Main Activity
                        openMainActivity()

                    } else {

                        // Auth Activity
                        openAuthActivity()

                    }

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