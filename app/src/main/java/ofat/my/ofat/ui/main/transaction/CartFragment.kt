package ofat.my.ofat.ui.main.transaction


import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.TxShortView
import ofat.my.ofat.ui.main.goods.GoodViewModel

class CartFragment : Fragment() {

    var viewModel: GoodViewModel? = null

    private lateinit var table: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.search_result)
        activity?.let {
            viewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
            init(this.view!!)
        }
    }

    private fun init(view: View) {
        table = view.findViewById(R.id.cartList)
        viewModel?.cart?.observe(this, Observer {
            if (CollectionUtils.mapIsNotEmpty(it)) {
                var index = 1
                var totalPrice = 0.0
                var totalQuantity = 0.0
                for (entry in it!!.entries) {
                    val row = TableRow(view.context)
                    val textView = TextView(view.context)
                    val currElement = makeTxShortView(entry)
                    makeTextView(textView, currElement, index)
                    row.layoutParams =
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                    row.addView(textView)
                    table.addView(row)
                    val divider = View(view.context, null, R.style.Divider)
                    table.addView(divider)
                    totalQuantity += entry.value.first
                    totalPrice += entry.value.second * entry.value.first
                    index++
                }
                val row = TableRow(view.context)
                val textView = TextView(view.context)
                makeTotalTextView(textView, totalQuantity, totalPrice)
                row.layoutParams =
                    TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                row.addView(textView)
                table.addView(row)
            }
        })
    }

    private fun makeTxShortView(entry: MutableMap.MutableEntry<Good, Pair<Double, Double>>): TxShortView {
        return TxShortView(entry.key, entry.value.first, entry.value.second)
    }

    private fun makeTextView(textView : TextView, currElement : TxShortView, index: Int) {
        textView.text = makeText(currElement, index)
        textView.textSize = 20F
        textView.isElegantTextHeight = true
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.setSingleLine(false)
        textView.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryText)))
        val lParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        lParams.setMargins(0, 0, 0, 3)
        textView.layoutParams = lParams
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0)
    }

    private fun makeText(currElement: TxShortView, index: Int): CharSequence? {
        return "$index.${currElement.good.name}${OfatConstants.HORIZONTAL_DIVIDER}${currElement.price} ${currElement.quantity} ${currElement.price * currElement.quantity}"
    }

    private fun makeTotalTextView(textView : TextView, quantity: Double, price: Double) {
        textView.text = makeTotalText(quantity, price)
        textView.textSize = 15F
        textView.isElegantTextHeight = true
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.setSingleLine(false)
        textView.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryText)))
        val lParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        lParams.setMargins(0, 0, 0, 3)
        textView.layoutParams = lParams
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0)
    }

    private fun makeTotalText(quantity: Double, price: Double): CharSequence? {
        return "  ИТОГО:      $quantity       $price"
    }
}
