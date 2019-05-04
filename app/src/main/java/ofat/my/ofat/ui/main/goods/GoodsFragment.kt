package ofat.my.ofat.ui.main.goods

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.AddGoodResponse
import ofat.my.ofat.api.response.GetGoodResponse
import ofat.my.ofat.api.response.GetGoodShortViewResponse
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.GoodStatus
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import ofat.my.ofat.persistence.OfatDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class GoodsFragment : Fragment() {

    private lateinit var goodViewModel: GoodViewModel

    private lateinit var foundListViewModel: FoundListViewModel

    private var barcode: String? = null

    private var goodIdET: TextInputEditText? = null

    private var goodNameET: TextInputEditText? = null

    private var incomeDateET: TextInputEditText? = null

    private var storedET: TextInputEditText? = null

    private var sellPackagingET: TextInputEditText? = null

    private var buyQuantityET: TextInputEditText? = null

    private var sellQuantityET: TextInputEditText? = null

    private var buyPriceET: TextInputEditText? = null

    private var priceET: TextInputEditText? = null

    private var barCodeET: TextInputEditText? = null

    private var manufacturerET: TextInputEditText? = null

    private var commentsET: TextInputEditText? = null

    private var addGoodBT: FloatingActionButton? = null

    private var searchGoodBT: FloatingActionButton? = null

    private var good: Good? = null

    private var fromFast = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.goods_fragment, container, false)
        (activity as MainActivity).setCurrentTitle(R.string.goods)
        barcode = arguments?.getString("barcode")
        fromFast = arguments?.getBoolean("fromFast") ?: false
        goodViewModel = ViewModelProviders.of(this).get(GoodViewModel::class.java)
        goodViewModel.barcode.observe(this, Observer<String> {
            if (it != null) {
                barcode = it
            }
        })
        good = arguments?.getParcelable<Good>("good")
        initViews(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        foundListViewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
    }

    private fun initViews(view: View) {

        goodIdET = view.findViewById(R.id.good_id_edit_text)
        goodNameET = view.findViewById(R.id.good_name_edit_text)
        UtilUI.clearTextField(goodNameET, context!!)

        incomeDateET = view.findViewById(R.id.good_incomeDate_edit_text)
        UtilUI.setDateToField(incomeDateET!!, activity!!)

        storedET = view.findViewById(R.id.good_stored_edit_text)
        UtilUI.clearTextField(storedET, context!!)

        sellPackagingET = view.findViewById(R.id.good_sellPackaging_edit_text)
        UtilUI.clearTextField(sellPackagingET, context!!)

        buyQuantityET = view.findViewById(R.id.good_buyQuantity_edit_text)
        UtilUI.clearTextField(buyQuantityET, context!!)

        sellQuantityET = view.findViewById(R.id.good_sellQuantity_edit_text)
        UtilUI.clearTextField(sellQuantityET, context!!)

        buyPriceET = view.findViewById(R.id.good_buyPrice_edit_text)
        UtilUI.clearTextField(buyPriceET, context!!)

        priceET = view.findViewById(R.id.good_price_edit_text)
        UtilUI.clearTextField(priceET, context!!)

        barCodeET = view.findViewById(R.id.good_barCode_edit_text)
        barCodeET?.setText(barcode)
        UtilUI.clearTextField(barCodeET, context!!)

        manufacturerET = view.findViewById(R.id.good_manufacturer_edit_text)
        UtilUI.clearTextField(manufacturerET, context!!)

        commentsET = view.findViewById(R.id.good_comments_edit_text)
        UtilUI.clearTextField(commentsET, context!!)

        addGoodBT = view.findViewById(R.id.addGood)
        addGoodBT?.setOnClickListener {
            processRequestAdd()
        }
        if (fromFast) {
            addGoodBT?.hide()
        }

        searchGoodBT = view.findViewById(R.id.searchGood)
        searchGoodBT?.setOnClickListener {
            processSearch()
        }
        if (good != null) {
            searchGoodBT?.hide()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun processSearch() {
        val reqMap = mutableMapOf<String, Any?>()
        reqMap["barCode"] = ExtractUtil.v(barCodeET!!) as Any?
        reqMap["buyPrice"] = ExtractUtil.v(buyPriceET!!)?.toDoubleOrNull() as Any?
        reqMap["buyQuantity"] = ExtractUtil.v(buyQuantityET!!)?.toIntOrNull() as Any?
        reqMap["goodComments"] = ExtractUtil.v(commentsET!!) as Any?

        val incomeDate = ExtractUtil.v(incomeDateET!!)
        reqMap["incomeDate"] = if (incomeDate != null) SimpleDateFormat("dd.MM.yyyy").parse(incomeDate) else null

        reqMap["manufacturer"] = ExtractUtil.v(manufacturerET!!) as Any?
        reqMap["name"] = ExtractUtil.v(goodNameET!!) as Any?
        reqMap["price"] = ExtractUtil.v(priceET!!)?.toDoubleOrNull() as Any?
        reqMap["sellPackaging"] = ExtractUtil.v(sellPackagingET!!) as Any?
        reqMap["sellQuantity"] = ExtractUtil.v(sellQuantityET!!)?.toIntOrNull() as Any?
        reqMap["stored"] = ExtractUtil.v(storedET!!)?.toIntOrNull() as Any?
        val call = OfatApplication.goodApi?.getGoods(reqMap)
        call?.enqueue(object : Callback<GetGoodShortViewResponse>{
            override fun onFailure(call: Call<GetGoodShortViewResponse>, t: Throwable) {
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<GetGoodShortViewResponse>,
                response: Response<GetGoodShortViewResponse>
            ) {
                if (response.body() != null && response.body()?.success != null) {
                    foundListViewModel.foundList.value = response.body()?.success as MutableCollection<ShortView>
                    val bundle = Bundle()
                    bundle.putBoolean("fromFast", fromFast)
                    view?.findNavController()?.navigate(R.id.foundListFragment, bundle)
                } else if (response.body() != null && response.body()?.errors != null) {
                    Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun processRequestAdd() {
        if (UtilUI.checkTextInputFields(arrayOf(goodNameET!!))) {
            val good = makeGood()
            val call = OfatApplication.goodApi?.createGood(good)
            call?.enqueue(object : Callback<AddGoodResponse>{
                override fun onFailure(call: Call<AddGoodResponse>, t: Throwable) {
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<AddGoodResponse>, response: Response<AddGoodResponse>) {
                    when {
                        response.body()?.success != null -> {
                            response.body()?.success?.forEach { (key, value) ->
                                good.id = key
                                OfatDatabase.getInstance(this@GoodsFragment.context!!).goodDao().save(good)
                                Toast.makeText(this@GoodsFragment.context, value, Toast.LENGTH_SHORT).show()
                            }
                        }
                        response.body()?.errors != null -> Toast.makeText(this@GoodsFragment.context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@GoodsFragment.context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                    }
                    view?.findNavController()?.navigate(R.id.menuFragment)
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun makeGood() : Good {
        val good = Good()
        good.barCode = ExtractUtil.v(barCodeET!!)
        good.buyPrice = ExtractUtil.v(buyPriceET!!)?.toDoubleOrNull()
        good.buyQuantity = ExtractUtil.v(buyQuantityET!!)?.toDoubleOrNull()
        good.goodComments = ExtractUtil.v(commentsET!!)
        good.incomeDate = SimpleDateFormat("dd.MM.yyyy", OfatApplication.LOCALE).parse(ExtractUtil.v(incomeDateET!!))
        good.manufacturer = ExtractUtil.v(manufacturerET!!)
        good.name = ExtractUtil.v(goodNameET!!)
        good.price = ExtractUtil.v(priceET!!)?.toDoubleOrNull()
        good.sellPackaging = ExtractUtil.v(sellPackagingET!!)
        good.sellQuantity = ExtractUtil.v(sellQuantityET!!)?.toDoubleOrNull()
        good.stored = ExtractUtil.v(storedET!!)?.toIntOrNull()
        good.status = GoodStatus.ACTIVE
        return good
    }
}
