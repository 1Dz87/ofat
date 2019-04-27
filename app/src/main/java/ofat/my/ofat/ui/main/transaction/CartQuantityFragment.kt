package ofat.my.ofat.ui.main.transaction

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.model.Good
import ofat.my.ofat.ui.main.goods.GoodViewModel

class CartQuantityFragment : Fragment() {

    private var headerTV: TextView? = null

    private var quantity: EditText? = null

    private var price: EditText? = null

    private var backBt: FloatingActionButton? = null

    private var toCartBt: FloatingActionButton? = null

    private var viewModel: GoodViewModel? = null

    private var good: Good? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart_quantity, container, false)
        good = arguments?.getParcelable("good")
        headerTV = view.findViewById(R.id.cartGoodHeader)
        headerTV?.text = good?.name
        quantity = view.findViewById(R.id.fast_quantity_text)
        price = view.findViewById(R.id.price_edit_text)
        price?.setText(good?.price?.toString() ?: "")
        backBt = view.findViewById(R.id.back)
        toCartBt = view.findViewById(R.id.btToCart)
        initButtons()
        return view
    }

    private fun initButtons() {
        backBt?.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }
        toCartBt?.setOnClickListener {
            if (UtilUI.checkTextFields(arrayOf(quantity!!, price!!))) {
                val txQuantity = ExtractUtil.v(quantity!!)?.toDouble()!!
                val txPrice = ExtractUtil.v(price!!)?.toDouble()!!
                viewModel?.getBuyCart()?.value!![good!!] = Pair<Double, Double>(txQuantity, txPrice)
                view?.findNavController()?.navigate(R.id.fragment_cart)
            }
        }
        view?.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                val dialog = this.let {
                    AlertDialog.Builder(it.context) }
                dialog.setTitle("Выход")
                dialog.setMessage("Выйти без сохранения?")
                dialog.setPositiveButton(OfatConstants.YES) { dialog1, _ ->
                    dialog1.dismiss()
                    val navigator = this@CartQuantityFragment.view?.findNavController()
                    navigator?.popBackStack(R.id.menuFragment, false)
                    navigator?.navigate(R.id.menuFragment)
                }
                dialog.setNegativeButton(OfatConstants.NO) { dialog1, _ ->
                    dialog1.dismiss()
                    }
                dialog.show()
            }
            false
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
    }


}
