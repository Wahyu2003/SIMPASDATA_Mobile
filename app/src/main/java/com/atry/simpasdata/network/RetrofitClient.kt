import com.atry.simpasdata.network.ApiClient
import com.atry.simpasdata.network.CostumTrustManager
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitClient {

    private val trustManager: X509TrustManager = CostumTrustManager()

    private val sslContext: SSLContext = SSLContext.getInstance("TLS").apply {
        init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private val gson = GsonBuilder().setLenient().create()


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.17/") // Sesuaikan dengan URL backend Anda
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getInstance(): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

}
