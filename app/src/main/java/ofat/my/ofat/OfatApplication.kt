package ofat.my.ofat

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.room.Room
import com.google.gson.GsonBuilder
import ofat.my.ofat.api.AuthApi
import ofat.my.ofat.api.GoodsApi
import ofat.my.ofat.api.TxApi
import ofat.my.ofat.api.UserApi
import ofat.my.ofat.logging.FileLoggingTree
import okhttp3.Credentials
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import android.os.Build

class OfatApplication : Application() {

    private var httpClient: OkHttpClient? = null

    companion object {
        var authApi: AuthApi? = null
        var userApi: UserApi? = null
        var goodApi: GoodsApi? = null
        var txApi: TxApi? = null
        var LOCALE: Locale? = Locale.UK
        val modules: Properties = Properties()
        val MODULES = "modules"
        val sharedStore: MutableMap<String, Any> = mutableMapOf()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(FileLoggingTree(), Timber.DebugTree())
        httpClient = makeHttpClient()
        modules.load(assets.open("modules.properties"))
        apiCreation()
        LOCALE = getCurrentLocale(this)
    }

    private fun makeHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor { chain ->
            val originalRequest = chain.request()

            val builder = originalRequest.newBuilder().header(
                "Authorization",
                Credentials.basic("test", "test")
            )
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }.build()
    }

    private fun apiCreation() {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.client(httpClient)
        retrofitBuilder.addConverterFactory(gsonConverterFactory())
        val serverUrl = "95.46.204.135:27017"
        //val serverUrl = "192.168.0.103:8080"
        val httpUrl = HttpUrl.parse("http://$serverUrl/ofat/")
        if (httpUrl == null) {
            Toast.makeText(this, "Настройки невалидны", Toast.LENGTH_LONG).show()
            return
        }
        retrofitBuilder.baseUrl(httpUrl)
        val retrofit = retrofitBuilder.build()
        makeApis(retrofit)
    }

    private fun makeApis(retrofit: Retrofit?) {
        authApi = retrofit?.create<AuthApi>(AuthApi::class.java)
        userApi = retrofit?.create<UserApi>(UserApi::class.java)
        goodApi = retrofit?.create<GoodsApi>(GoodsApi::class.java)
    }

    private fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }

    private fun gsonConverterFactory(): Converter.Factory {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

}