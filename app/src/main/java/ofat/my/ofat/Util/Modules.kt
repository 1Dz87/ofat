package ofat.my.ofat.Util

import ofat.my.ofat.R
import java.lang.RuntimeException


enum class Module (var property: String, var button: Int?, var menu: Int) {

    FINANCES("fin", R.id.btFinances, R.id.nav_finances),

    GOODS("good", R.id.btGoods, R.id.nav_goods),

    POINTS("pts", R.id.btPoints, R.id.nav_points),

    PROVIDERS("provs", R.id.btProviders, R.id.nav_providers),

    WORKERS("wks", R.id.btWorkers, R.id.nav_workers),

    USERS("us", R.id.btUsers, R.id.nav_users),

    SCANNER("sc", R.id.btScanner, R.id.nav_scanner),

    MAIN("main", null, R.id.nav_main),

    SELL("sell", R.id.btSell, R.id.nav_sell);
    companion object {
        fun getModuleByProp(prop: String) : Module {
            return when (prop) {
                "fin" -> FINANCES
                "good" -> GOODS
                "pts" -> POINTS
                "provs" -> PROVIDERS
                "wks" -> WORKERS
                "us" -> USERS
                "sc" -> SCANNER
                "main" -> MAIN
                "sell" -> SELL
                else -> {
                    throw RuntimeException()
                }
            }
        }
    }
}