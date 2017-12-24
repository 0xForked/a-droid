package id.asmith.someapp.util


/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */
object AppConstants {

    //Email validation
    val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    //Local data db
    val DB_NAME = "QueueApp"
    val DB_VERSION = 1
    val DB_TABLE = "user"
    val KEY_ID = "id"
    val KEY_UID = "uid"
    val KEY_USERNAME ="username"
    val KEY_NAME ="name"
    val KEY_EMAIL ="email"
    val KEY_PHONE ="phone"
    val KEY_TOKEN = "token"
    val KEY_LOGGED = "isLoggedIn"
    val KEY_CREATED = "created_at"
    val KEY_UPDATED ="updated_at"

    //Remote data
    private val BASE_API_URL = "http://192.168.43.70/project/queue-api/"

    private val AUTH_ROUTE = "api/v1/auth/"
    private val USER_ROUTE = "api/v1/user/"
    private val USER_PASSWORD_ROUTE = "api/v1/user/password/"

    val AUTH_API_URL = BASE_API_URL + AUTH_ROUTE
    val USER_API_URL = BASE_API_URL + USER_ROUTE
    val USER_PASSWORD_API_URL = BASE_API_URL + USER_PASSWORD_ROUTE


}