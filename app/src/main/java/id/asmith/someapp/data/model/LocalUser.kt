package id.asmith.someapp.data.model


/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */


data class LocalUser(
            val uid: String,
            val username: String,
            val name: String,
            val email: String,
            val phone: String,
            val token: String,
            val logged: String,
            val created: String,
            val updated: String
    )

