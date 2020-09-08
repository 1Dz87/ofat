package ofat.my.ofat.ui.main.goods

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import ofat.my.ofat.api.response.AddGoodResponse
import ofat.my.ofat.api.response.GetGoodShortViewResponse
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import ofat.my.ofat.Util.*
import ofat.my.ofat.api.response.GoodsGroupNamesResponse
import ofat.my.ofat.model.*
import ofat.my.ofat.persistence.OfatDatabase
import retrofit2.Response


class GoodsFragment : Fragment() {

    private lateinit var goodViewModel: GoodViewModel

    private lateinit var foundListViewModel: FoundListViewModel

    private var barcode: String? = null

    private var goodIdET: TextInputEditText? = null

    private var goodNameET: TextInputEditText? = null

    private var goodsGroupSpinner: Spinner? = null

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

    private var fromBk = false

    private var progress: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.goods_fragment, container, false)
        (activity as MainActivity).setCurrentTitle(R.string.goods)
        barcode = arguments?.getString("barcode")
        fromFast = arguments?.getBoolean("fromFast") ?: false
        fromBk = arguments?.getBoolean("fromBk") ?: false
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

    private fun initGoodsGroupsNamesList() {
        val call = OfatApplication.goodsGroupApi?.getGoodsGroupsNames()
        UtilUI.showProgress(progress!!)
        call?.enqueue(object : Callback<GoodsGroupNamesResponse>{
            override fun onFailure(call: Call<GoodsGroupNamesResponse>, t: Throwable) {
                UtilUI.showProgress(progress!!)
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GoodsGroupNamesResponse>, response: Response<GoodsGroupNamesResponse>) {
                UtilUI.showProgress(progress!!)
                if (response.body() != null && response.body()?.success != null) {
                    val groups = response.body()?.success as List<GoodsGroup>
                    goodsGroupSpinner?.adapter =
                        ArrayAdapter<GoodsGroup>(activity!!, R.layout.spinner_item, R.id.spinner_item_tv, groups)
                } else if (response.body() != null && response.body()?.errors != null) {
                    Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, WebUtil.checkUnauthCode(response, OfatConstants.UNKNOWN_ERROR, null), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initViews(view: View) {
        progress = view.findViewById(R.id.progressBarGF)

        goodIdET = view.findViewById(R.id.good_id_edit_text)
        goodNameET = view.findViewById(R.id.good_name_edit_text)
        UtilUI.clearTextField(goodNameET, context!!)

        goodsGroupSpinner = view.findViewById(R.id.goods_group_spinner)
        initGoodsGroupsNamesList()

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
        if (fromFast || fromBk) {
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
        reqMap["group"] = if ((goodsGroupSpinner?.selectedItem) != null) (goodsGroupSpinner?.selectedItem as GoodsGroup).id else null
        val filteredMap = reqMap.filter { it.value != null }
        if (CollectionUtils.mapIsNotEmpty(filteredMap as MutableMap<*, *>)) {
            val call = OfatApplication.goodApi?.getGoods(reqMap)
            UtilUI.showProgress(progress!!)
            call?.enqueue(object : Callback<GetGoodShortViewResponse> {
                override fun onFailure(call: Call<GetGoodShortViewResponse>, t: Throwable) {
                    UtilUI.showProgress(progress!!)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<GetGoodShortViewResponse>,
                    response: Response<GetGoodShortViewResponse>
                ) {
                    UtilUI.showProgress(progress!!)
                    if (response.body() != null && response.body()?.success != null) {
                        foundListViewModel.foundList.value = response.body()?.success as MutableCollection<ShortView>
                        val bundle = Bundle()
                        bundle.putBoolean("fromFast", fromFast)
                        bundle.putBoolean("fromBk", fromBk)
                        view?.findNavController()?.navigate(R.id.foundListFragment, bundle)
                    } else if (response.body() != null && response.body()?.errors != null) {
                        Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, WebUtil.checkUnauthCode(response, OfatConstants.UNKNOWN_ERROR, null), Toast.LENGTH_SHORT).show()
                    }
                }

            })
        } else {
            Toast.makeText(context, OfatConstants.NO_REQUISITES_ERROR, Toast.LENGTH_LONG).show()
        }
    }

    private fun processRequestAdd() {
        if (UtilUI.checkTextInputFields(arrayOf(goodNameET!!))) {
            val good = makeGood()
            val call = OfatApplication.goodApi?.createGood(good)
            UtilUI.showProgress(progress!!)
            call?.enqueue(object : Callback<AddGoodResponse>{
                override fun onFailure(call: Call<AddGoodResponse>, t: Throwable) {
                    UtilUI.showProgress(progress!!)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<AddGoodResponse>, response: Response<AddGoodResponse>) {
                    UtilUI.showProgress(progress!!)
                    when {
                        response.body()?.success != null -> {
                            response.body()?.success?.forEach { (key, value) ->
                                good.id = key
                                OfatDatabase.getInstance(this@GoodsFragment.context!!).goodDao().save(good)
                                Toast.makeText(this@GoodsFragment.context, value, Toast.LENGTH_SHORT).show()
                            }
                        }
                        response.body()?.errors != null -> Toast.makeText(this@GoodsFragment.context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, WebUtil.checkUnauthCode(response, OfatConstants.UNKNOWN_ERROR, null), Toast.LENGTH_SHORT).show()
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
        good.group = if (goodsGroupSpinner?.selectedItem != null) goodsGroupSpinner?.selectedItem as GoodsGroup else GoodsGroup.createEmpty()
        return good
    }

}
