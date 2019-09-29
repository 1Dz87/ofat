package ofat.my.ofat.ui.main.transaction


import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.TransactionResponse
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.Transaction
import ofat.my.ofat.model.TransactionStatus
import ofat.my.ofat.model.TransactionType
import ofat.my.ofat.ui.main.goods.GoodViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashSet

class CartFragment : Fragment() {

    var viewModel: GoodViewModel? = null

    private var table: TableLayout? = null

    private var btBack : Button? = null

    private var btTx : Button? = null

    private var progress : ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.cart)
        activity?.let {
            viewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
            init(this.view!!)
        }
    }

    private fun init(view: View) {
        table = view.findViewById(R.id.cartList)
        progress = view.findViewById(R.id.progressBarTN)
        btBack = view.findViewById(R.id.btBackToSc)
        btBack!!.setOnClickListener {
            view.findNavController().navigateUp()
        }
        btTx = view.findViewById(R.id.btTx)
        btTx!!.setOnClickListener{ transactionProcess()}
        viewModel?.cart?.observe(this,
            Observer<MutableMap<Good, Pair<Double, Double>>> { t -> buildViewTable(t) })
    }

    private fun transactionProcess() {
        val transactions = collectTransactionsData()
        val call = OfatApplication.txApi?.multipleTransaction(transactions)
        UtilUI.showProgress(progress!!)
        call?.enqueue(object : Callback<TransactionResponse> {
            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                UtilUI.showProgress(progress!!)
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                UtilUI.showProgress(progress!!)
                if (response.body() != null && response.body()?.success != null) {
                    Toast.makeText(context, "Транзакция завершена", Toast.LENGTH_LONG).show()
                    viewModel?.clearCart()
                    view?.findNavController()?.navigateUp()
                } else {
                    if (response.body() != null && response.body()?.errors != null) {
                        Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun collectTransactionsData() : Set<Transaction> {
        val cart = viewModel?.cart?.value
        val result = HashSet<Transaction>()
        cart!!.entries.forEach { entry ->
            val tx = Transaction()
            tx.good = entry.key
            tx.date = Date()
            tx.quantity = entry.value.first
            tx.price = entry.value.second
            tx.status = TransactionStatus.ACCEPTED
            tx.type = TransactionType.OUT
            result.add(tx)
        }
        return result
    }

    private fun buildViewTable(it : MutableMap<Good, Pair<Double, Double>>?) {
        if (CollectionUtils.mapIsNotEmpty(it)) {
            var index = 1
            var totalPrice = 0.0
            var totalQuantity = 0.0
            it!!.entries.forEach { entry ->
                val total = entry.value.first * entry.value.second
                val row = TableRow(view?.context)
                val nameTextView = makeTextView(entry.key.name!!, Gravity.START)
                val qTextView = makeTextView((entry.value.first.toInt()).toString(), Gravity.CENTER_HORIZONTAL)
                val priceTextView = makeTextView(String.format("%.2f", entry.value.second), Gravity.CENTER_HORIZONTAL)
                val totalTextView = makeTextView(String.format("%.2f", total), Gravity.END)
                row.layoutParams =
                    TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                row.gravity = Gravity.CENTER_HORIZONTAL
                row.addView(nameTextView)
                row.addView(priceTextView)
                row.addView(qTextView)
                row.addView(totalTextView)
                table?.addView(row)
                val divider = View(view?.context, null, R.style.Divider)
                table?.addView(divider)
                totalQuantity += entry.value.first
                totalPrice += entry.value.second * entry.value.first
                index++
            }
            val row = TableRow(view?.context)
            row.layoutParams =
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
            val nameTextView = makeTextView("ИТОГО:", Gravity.START)
            val qTextView = makeTextView(totalQuantity.toInt().toString(), Gravity.CENTER_HORIZONTAL)
            val priceTextView = makeTextView(String.format("%.2f", totalPrice), Gravity.CENTER_HORIZONTAL)
            val spaceTV = makeTextView("   ", Gravity.END)
            row.addView(nameTextView)
            row.addView(spaceTV)
            row.addView(qTextView)
            row.addView(priceTextView)
            table?.addView(row)
        }
    }

    private fun makeTextView(value : String, gravity: Int) : TextView {
        val textView = TextView(view?.context)
        textView.text = value
        textView.textSize = 15F
        textView.isElegantTextHeight = true
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.setSingleLine(false)
        textView.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryText)))
        val lParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        lParams.setMargins(0, 0, 0, 3)
        textView.layoutParams = lParams
        textView.gravity = gravity
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0)
        return textView
    }

}
