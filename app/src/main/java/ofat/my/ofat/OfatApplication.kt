package ofat.my.ofat

import android.app.Application
import android.content.Context
import android.os.Build
import ofat.my.ofat.api.*
import ofat.my.ofat.logging.FileLoggingTree
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber
import java.util.*

class OfatApplication : Application() {

    companion object {
        var httpClient: OkHttpClient? = null
        var authApi: AuthApi? = null
        var userApi: UserApi? = null
        var goodApi: GoodsApi? = null
        var templatesApi: TemplatesApi? = null
        var txApi: TxApi? = null
        var pointsApi: PointsApi? = null
        var goodsGroupApi: GoodsGroupApi? = null
        var creds: String? = null
        var LOCALE: Locale? = Locale.UK
        val modules: Properties = Properties()
        val MODULES = "modules"
        val sharedStore: MutableMap<String, Any> = mutableMapOf()

        fun makeApis(retrofit: Retrofit?) {
            authApi = retrofit?.create(AuthApi::class.java)
            userApi = retrofit?.create(UserApi::class.java)
            goodApi = retrofit?.create(GoodsApi::class.java)
            templatesApi = retrofit?.create(TemplatesApi::class.java)
            txApi = retrofit?.create(TxApi::class.java)
            pointsApi = retrofit?.create(PointsApi::class.java)
            goodsGroupApi = retrofit?.create(GoodsGroupApi::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(FileLoggingTree(), Timber.DebugTree())
        modules.load(assets.open("modules.properties"))
        LOCALE = getCurrentLocale(this)
    }

    private fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
    }
}