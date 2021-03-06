package ofat.my.ofat

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import ofat.my.ofat.Util.Module
import ofat.my.ofat.api.response.AuthLogoutResponse
import ofat.my.ofat.permission.requestExternalStoragePermission
import ofat.my.ofat.persistence.Synchronizer
import ofat.my.ofat.ui.main.FoundListViewModel
import ofat.my.ofat.ui.main.MainViewModel
import ofat.my.ofat.ui.main.goods.GoodViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    lateinit var actionBarDrawer : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var navView : NavigationView
    lateinit var model: MainViewModel
    lateinit var foundModel: FoundListViewModel
    lateinit var goodViewModel: GoodViewModel
    var logedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestExternalStoragePermission(this)
        }
        thread {
            synchronizeDataBase()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)
        foundModel = ViewModelProviders.of(this).get(FoundListViewModel::class.java)
        goodViewModel = ViewModelProviders.of(this).get(GoodViewModel::class.java)
        setupDrawer()
    }

    fun setCurrentTitle(title: Any) {
        model.currentTitle.value = title
        when (title) {
            is Int -> supportActionBar?.setTitle(model.currentTitle.value!! as Int)
            is CharSequence -> supportActionBar?.setTitle(model.currentTitle.value!! as CharSequence)
            is String -> supportActionBar?.setTitle(model.currentTitle.value!! as String)
            else -> {
                Timber.e("Title type is wrong: $title")
                supportActionBar?.setTitle(R.string.app_name)
            }
        }
    }

    private fun setupDrawer() {
        actionBarDrawer = object : ActionBarDrawerToggle(this, drawerLayout, R.string.side_menu_open, R.string.side_menu_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                setCurrentTitle(R.string.side_menu_open)
                invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                setCurrentTitle(model.currentTitle.value!!)
                invalidateOptionsMenu()
            }
        }
        actionBarDrawer.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(actionBarDrawer)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawer.syncState()
    }

    fun onMenuClick(item: MenuItem) {
        val id = item.itemId
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        when (id) {
            R.id.nav_sell -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.fastTxScFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_finances -> {
                navController.popBackStack(R.id.menuFragment, false)
//                onFinancesClick(navController)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_scanner -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.scannerFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_main -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.menuFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_goods -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.selectionBeforeGoodFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_points -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.goodsFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_providers -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.goodsFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_workers -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.goodsFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_users -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.usersFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_about -> {
                navController.popBackStack(R.id.menuFragment, false)
                navController.navigate(R.id.goodsFragment)
                drawer.closeDrawer(GravityCompat.START)
            }
            R.id.nav_exit -> {
                this.finish()
                System.exit(0)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawer.onOptionsItemSelected(item)) {
            val id = item.itemId
            val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
            when (id) {
                R.id.nav_sell -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.fastTxScFragment)
                    return true
                }
                R.id.nav_finances -> {
                    navController.popBackStack(R.id.menuFragment, false)
//                    onFinancesClick(navController)
                    return true
                }
                R.id.nav_scanner -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.scannerFragment)
                    return true
                }
                R.id.nav_main -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.menuFragment)
                    return true
                }
                R.id.nav_goods -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.selectionBeforeGoodFragment)
                    return true
                }
                R.id.nav_points -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.goodsFragment)
                    return true
                }
                R.id.nav_providers -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.goodsFragment)
                    return true
                }
                R.id.nav_workers -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.goodsFragment)
                    return true
                }
                R.id.nav_users -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.usersFragment)
                    return true
                }
                R.id.nav_about -> {
                    navController.popBackStack(R.id.menuFragment, false)
                    navController.navigate(R.id.goodsFragment)
                    return true
                }
                R.id.nav_exit -> {
                    this.finish()
                    exitProcess(0)
                } else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
/*            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)*/
            true
        } else return super.onOptionsItemSelected(item)

    }

    fun changeMenuNavigatorOnLogin() {
        logedIn = true
        navView.menu.findItem(R.id.nav_login).isVisible = false
        val applied = OfatApplication.modules.getProperty(OfatApplication.MODULES).split(",")
        for (i in applied) {
            val module = Module.getModuleByProp(i)
            navView.menu.findItem(module.menu).isVisible = true
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (logedIn) {
            changeMenuNavigatorOnLogin()
        }
        super.onConfigurationChanged(newConfig)
    }

    private fun synchronizeDataBase() {
        val synhronizeReq: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(Synchronizer::class.java, 1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueue(synhronizeReq)
//        WorkManager.getInstance(this).getWorkInfoByIdLiveData(synhronizeReq.id)
//            .observe(this, androidx.lifecycle.Observer { workInfo ->
//                if (workInfo == null || workInfo.state != WorkInfo.State.SUCCEEDED) {
//                    Toast.makeText(this, "Ошибка синхронизации базы данных", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(this, "Синхронизирована база данных", Toast.LENGTH_LONG).show()
//                }
//            })
    }

    override fun onStop() {
        /*supportFragmentManager.fragments.forEach {
            if (it != null) {
                supportFragmentManager.beginTransaction().remove(it).commit()
            }
        }*/
        val call = OfatApplication.authApi?.logout()
        call?.enqueue(object : Callback<AuthLogoutResponse> {
            override fun onFailure(call: Call<AuthLogoutResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<AuthLogoutResponse>, response: Response<AuthLogoutResponse>) {
            }
        })
        super.onStop()
    }
}
