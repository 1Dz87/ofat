package ofat.my.ofat.ui.main.transaction

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
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
import java.text.SimpleDateFormat
import java.util.*

class TransactionFragment : Fragment() {

    lateinit var goodViewModel: GoodViewModel

    private var progress: ProgressBar? = null

    private var good: Good? = null

    private var good_ET: TextInputEditText? = null

    private var price_ET: TextInputEditText? = null

    private var quantity_ET: TextInputEditText? = null

    private var date_ET: TextInputEditText? = null

    private var time_ET: TextInputEditText? = null

    private var partner_ET: TextInputEditText? = null

    private var comment_ET: TextInputEditText? = null

    private var status: TransactionStatus? = null

    private var type: TransactionType? = null

    private var btTx: Button? = null

    private var btCancel: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.transaction_fragment, container, false)
        if (good == null) {
            good = arguments?.getParcelable("good")!!
            type = TransactionType.values()[arguments?.getInt("type")!!]
            status = TransactionStatus.values()[arguments?.getInt("status")!!]
        }
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.transaction_title)
        goodViewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
        goodViewModel.currentGood.observe(this, Observer<Good> { t ->
            if (t != null)
                good = t
        })
    }

    private fun initView(view: View?) {
        progress = view?.findViewById(R.id.progressBarTN)

        good_ET = view?.findViewById(R.id.transaction_good_text)
        price_ET = view?.findViewById(R.id.price_edit_text)
        fillGoodsFields()

        quantity_ET = view?.findViewById(R.id.quantity_edit_text)

        date_ET = view?.findViewById(R.id.tx_date_edit_text)
        UtilUI.setDateToField(date_ET!!, activity!!)

        time_ET = view?.findViewById(R.id.tx_time_edit_text)
        UtilUI.setTimeToField(time_ET!!, activity!!)

        partner_ET = view?.findViewById(R.id.partner_text)
        //TODO: Navigate to partner fragment

        comment_ET = view?.findViewById(R.id.transaction_comment_text)

        btTx = view?.findViewById(R.id.btTx)
        btCancel = view?.findViewById(R.id.btCancel)

        initButtons()
    }

    private fun initButtons() {
        btTx?.setOnClickListener {
            if (UtilUI.checkTextFields(arrayOf(good_ET!!, price_ET!!, date_ET!!, time_ET!!, quantity_ET!!))) {
                val request = fillTxRequest()
                val call = OfatApplication.txApi?.doTransact(request)
                UtilUI.showProgress(progress!!)
                call?.enqueue(object : Callback<TransactionResponse> {
                    override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                        Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                        if (response.body() != null && response.body()?.success != null) {
                            val dialog = this.let { AlertDialog.Builder(activity) }
                            dialog.setTitle("Внимание!")
                            dialog.setMessage("Транзакция успешно проведена. Транзакции присвоен номер " +
                                    "${((response.body()?.success as Map)[OfatConstants.SUCCESS] as Transaction).id}")
                            dialog.setPositiveButton("OK") { dialog1, _ ->
                                dialog1.dismiss()
                                it.findNavController().navigateUp()
                            }
                            dialog.show()
                        } else {
                            Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                Toast.makeText(context, "Заполните обязательные поля и тип транзакции", Toast.LENGTH_LONG).show()
            }
        }

        btCancel?.setOnClickListener {
            view?.findNavController()?.navigateUp()
        }
    }

    private fun fillTxRequest(): Transaction {
        val request = Transaction()
        if (UtilUI.checkTextFields(arrayOf(quantity_ET!!, price_ET!!))) {
            request.good = good
            request.price = ExtractUtil.v(price_ET!!)?.toDouble()
            request.date = parseDate()
            request.status = status
            request.quantity = ExtractUtil.v(quantity_ET!!)?.toDouble()
            request.type = type
            //TODO: set the partner
        }
        return request
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate(): Date? {
        val dateString = ExtractUtil.v(date_ET!!) + ": " + ExtractUtil.v(time_ET!!)
        val parser = SimpleDateFormat("dd.MM.yyyy: " + DateUtils.FORMAT_SHOW_TIME)
        return parser.parse(dateString)
    }

    private fun fillGoodsFields() {
        if (good != null) {
            good_ET?.setText(good?.name)
            price_ET?.setText(good?.price.toString())
        }
        good_ET?.setOnClickListener {
            val dialog = this.let { AlertDialog.Builder(it.context) }
            dialog.setTitle("Внимание!")
            dialog.setMessage("Изменить товар?")
            dialog.setPositiveButton("Да") { dialog1, _ ->
                dialog1.dismiss()
                it.findNavController().navigateUp()
            }
            dialog.setNegativeButton("Отмена") { dialog1, _ ->
                dialog1.dismiss()
            }
            dialog.show()
        }
    }
}