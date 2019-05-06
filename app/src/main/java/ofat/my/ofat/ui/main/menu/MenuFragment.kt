package ofat.my.ofat.ui.main.menu

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity
import ofat.my.ofat.R
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.Util.Module
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.permission.requestCameraPermission
import ofat.my.ofat.ui.main.FoundListViewModel
import ofat.my.ofat.ui.main.MainViewModel
import ofat.my.ofat.ui.main.goods.GoodViewModel


class MenuFragment : androidx.fragment.app.Fragment(), View.OnKeyListener {
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        var result = false
        return if( keyCode == KeyEvent.KEYCODE_BACK ) {
            val dialog = this.let { AlertDialog.Builder(v?.context) }
            dialog.setTitle("Внимание!")
            dialog.setMessage("Вы действительно хотите выйти?")
            dialog.setPositiveButton("Да") { dialog1, _ ->
                dialog1.dismiss()
                result = true
            }
            dialog.setNegativeButton("Нет") { dialog1, _ ->
                dialog1.dismiss() }
            dialog.show()
            result
        } else {
            false
        }
    }

    private lateinit var btFinances: Button

    private lateinit var btGoods: Button

    private lateinit var btPoints: Button

    private lateinit var btProviders: Button

    private lateinit var btWorkers: Button

    private lateinit var btUsers: Button

    private lateinit var btScanner: Button

    private lateinit var btSell: Button

    var mainModel: MainViewModel? = null

    var foundModel: FoundListViewModel? = null

    var goodViewModel: GoodViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.start_menu_fragment, container, false)
        initModules(view)
        initButtons(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.side_menu_open)
        activity?.let {
            mainModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
            foundModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
            goodViewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
        }
    }

    private fun initModules(view: View) {
        val applied = OfatApplication.modules.getProperty(OfatApplication.MODULES).split(",")
        for (i in applied) {
            val module = Module.getModuleByProp(i)
            if (module != Module.MAIN) {
                view.findViewById<Button>(module.button!!).isVisible = true
            }
        }
    }

    private fun initButtons(view: View) {
        btFinances = view.findViewById(R.id.btFinances)
        btFinances.setOnClickListener { view.findNavController().navigate(R.id.bookkeepingListFragment) }

        btGoods = view.findViewById(R.id.btGoods)
        btGoods.setOnClickListener { view.findNavController().navigate(R.id.goodsFragment) }

        btPoints = view.findViewById(R.id.btPoints)
        btPoints.setOnClickListener { view.findNavController().navigate(R.id.menuFragment) }

        btProviders = view.findViewById(R.id.btProviders)
        btProviders.setOnClickListener { view.findNavController().navigate(R.id.menuFragment) }

        btWorkers = view.findViewById(R.id.btWorkers)
        btWorkers.setOnClickListener { view.findNavController().navigate(R.id.menuFragment) }

        btUsers = view.findViewById(R.id.btUsers)
        btUsers.setOnClickListener { view.findNavController().navigate(R.id.usersFragment) }

        btScanner = view.findViewById(R.id.btScanner)
        btScanner.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermission(this.activity!!)
            } else {
                view.findNavController().navigate(R.id.scannerFragment)
            }

        }

        btSell = view.findViewById(R.id.btSell)
        btSell.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                requestCameraPermission(this.activity!!)
            } else {
                goodViewModel?.clearCart()
                view.findNavController().navigate(R.id.fastTxScFragment)
            }
        }
    }

}