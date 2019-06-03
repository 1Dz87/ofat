package ofat.my.ofat

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import ofat.my.ofat.Util.Module
import ofat.my.ofat.permission.requestExternalStoragePermission
import ofat.my.ofat.ui.main.FoundListViewModel
import ofat.my.ofat.ui.main.MainViewModel
import ofat.my.ofat.ui.main.goods.GoodViewModel
import timber.log.Timber
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.GetBookkeepersSVResponse
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.permission.requestCameraPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                onFinancesClick(navController)
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
                    onFinancesClick(navController)
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
                    System.exit(0)
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

    private fun onFinancesClick(navController: NavController) {
        val call = OfatApplication.bookkeeperApi?.getBookkeepersSV()
        call?.enqueue(object : Callback<GetBookkeepersSVResponse> {
            @SuppressLint("ThrowableNotAtBeginning", "TimberExceptionLogging")
            override fun onFailure(call: Call<GetBookkeepersSVResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка сервера.", Toast.LENGTH_SHORT).show()
                Timber.e(t, t.message, t.cause)
            }

            override fun onResponse(
                call: Call<GetBookkeepersSVResponse>,
                response: Response<GetBookkeepersSVResponse>
            ) {
                if (response.body()?.success != null) {
                    foundModel.foundList.value = response.body()?.success as MutableCollection<ShortView>
                    navController.navigate(R.id.foundListFragment)
                } else {
                    Toast.makeText(this@MainActivity, "Группа не найдена.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
