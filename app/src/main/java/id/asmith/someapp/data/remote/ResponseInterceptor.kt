package id.asmith.someapp.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Agus Adhi Sumitro on 23/12/2017.
 * https://asmith.my.id
 * aasumitro@gmail.com
 */

class ResponseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val lResponse = chain.proceed(chain.request())

        when {
            lResponse.code() == 401 -> // unauthorized
                Log.d("interceptor", "401")
            lResponse.code() == 403 -> // forbidden
                Log.d("interceptor", "403")
            lResponse.code() == 404 -> // endpoint not found
                Log.d("interceptor", "404")
            lResponse.code() == 500 -> // internal server error
                Log.d("interceptor", "500")
            lResponse.code() == 502 -> // bad gateway
                Log.d("interceptor", "502")
            lResponse.code() == 503 -> // service unavailable
                Log.d("interceptor", "503")
            lResponse.code() == 504 -> // gateway timeout
                Log.d("interceptor", "504")
        }

        return lResponse
    }

}