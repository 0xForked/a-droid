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

        //Do thread
        val background = object : Thread() {
            override fun run() {
                try {

                    // Thread will sleep for 3 seconds
                    sleep((3 * 1000).toLong())

                    //Get local user data
                    val userData = SQLHandler(baseContext).getUserData()
                    val isLoggedIn = userData["logged"]

                    //cek user ever login before or user already login and
                    //did'nt clear hes session
                    if (isLoggedIn != null && isLoggedIn.toBoolean()) {

                        // if local data is not empty and
                        // user session still available
                        // Redirect him to Main Activity
                        openMainActivity()

                    } else {
                        // if user logged status has been destroy or
                        // user data is empty in our local table
                        // Auth Activity
                        openAuthActivity()

                    }

                } catch (e: Exception) {
                    //Print error in logcat (development requirement)
                    e.printStackTrace()

                }
            }
        }

        // start thread
        background.start()

    }

    fun openAuthActivity() {

        // redirect to another Auth Activity
        startActivity<AuthActivity>()

        //Remove activity
        finish()

    }

    fun openMainActivity() {

        // redirect to another Main Activity
        startActivity<MainActivity>()

        //Remove activity
        finish()

    }
}