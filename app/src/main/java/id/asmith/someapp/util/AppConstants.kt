package id.asmith.someapp.util


/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */
object AppConstants {

    val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")


    //Local data
    val STATUS_CODE_SUCCESS = "success"

    val STATUS_CODE_FAILED = "failed"

    val API_STATUS_CODE_LOCAL_ERROR = 0

    val DB_NAME = "queue.db"

    val PREF_NAME = "queue_pref"

    val NULL_INDEX = -1L

    val SEED_DATABASE_OPTIONS = "seed/options.json"

    val SEED_DATABASE_QUESTIONS = "seed/questions.json"

    //Remote data
    private val BASE_API_URL = "http://192.168.43.70/project/"

    private val BASE_ROUTE_API = "queue-api/api/v1/auth/"

    val AUTH_API_URL = BASE_API_URL + BASE_ROUTE_API

}