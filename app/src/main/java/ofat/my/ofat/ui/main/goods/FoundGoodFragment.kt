package ofat.my.ofat.ui.main.goods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.Util.DateUtil
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.DeleteGoodResponse
import ofat.my.ofat.api.response.GetGoodResponse
import ofat.my.ofat.model.Good
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FoundGoodFragment : Fragment() {

    lateinit var foundViewModel: FoundListViewModel
    lateinit var goodViewModel: GoodViewModel

    var id: TextView? = null
    var goodName: TextView? = null
    var incomeDate: TextView? = null
    var goodBarCode: TextView? = null
    var sellPackaging: TextView? = null
    var buyQuantity: TextView? = null
    var sellQuantity: TextView? = null
    var stored: TextView? = null
    var buyPrice: TextView? = null
    var price: TextView? = null
    var discount: TextView? = null
    var manufacturer: TextView? = null
    var progress: ProgressBar? = null
    var btDeleteGood: FloatingActionButton? = null
    var btEditGood: FloatingActionButton? = null
    var btTransaction: FloatingActionButton? = null

    var good: Good? = null
    var fromFast = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_good_found, container, false)
        progress = view.findViewById(R.id.progressBarGF)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        foundViewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
        goodViewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
        goodViewModel.currentGood.observe(this, Observer<Good> { t ->
            if (t != null)
                good = t
        })
        (activity as MainActivity).setCurrentTitle(R.string.foundGood_header)
        val id = arguments?.getString("goodId")!!.toLong()
        fromFast = arguments?.getBoolean("fromFast") ?: false
        loadCurrGood(id)
    }

    private fun loadCurrGood(id: Long) {
        val call = OfatApplication.goodApi?.getGoodById(id)
        UtilUI.showProgress(progress!!)
        call?.enqueue(object : Callback<GetGoodResponse> {
            override fun onFailure(call: Call<GetGoodResponse>, t: Throwable) {
                Toast.makeText(this@FoundGoodFragment.context, "Соединение разорвано или сервер не отвечает",  Toast.LENGTH_SHORT).show()
                UtilUI.returnToMenu(view)
            }

            override fun onResponse(call: Call<GetGoodResponse>, response: Response<GetGoodResponse>) {
                if (response.body() != null && response.body()?.success != null) {
                    goodViewModel.currentGood.value = response.body()?.success as Good
                    UtilUI.showProgress(progress!!)
                    initTextViews(view!!)
                    initButtons(view!!)
                } else {
                    if (response.body() != null && response.body()?.errors != null) {
                        Toast.makeText(this@FoundGoodFragment.context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                        UtilUI.returnToMenu(view)
                    }
                    Toast.makeText(this@FoundGoodFragment.context,"Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                    UtilUI.returnToMenu(view)
                }
            }
        })
    }

    private fun initButtons(view: View) {
        val bundle = Bundle()
        bundle.putParcelable("good", good)
        btDeleteGood = view.findViewById(R.id.deleteGood)
        btDeleteGood?.setOnClickListener { deleteGood()}

        btEditGood = view.findViewById(R.id.editGood)
        btEditGood?.setOnClickListener{ this@FoundGoodFragment.findNavController().navigate(R.id.goodsFragment, bundle) }

        if (fromFast) {
            btDeleteGood?.hide()
            btEditGood?.hide()
        }

        btTransaction = view.findViewById(R.id.transactionBt)
        btTransaction?.setOnClickListener{ makeTransaction() }
    }

    private fun makeTransaction() {
        val bundle = Bundle()
        bundle.putParcelable("good", good)
        if (fromFast) {
            val navigator = view?.findNavController()
            navigator?.popBackStack(R.id.fastTxScFragment, false)
            navigator?.navigate(R.id.fastTxCartQuantityFragment, bundle)
        } else {
            view?.findNavController()?.navigate(R.id.transactionStatusFragment, bundle)
        }
    }

    private fun deleteGood() {
        if (good != null) {
            val call = OfatApplication.goodApi?.deleteGood(good?.id!!)
            UtilUI.showProgress(progress!!)
            call?.enqueue(object : Callback<DeleteGoodResponse> {
                override fun onFailure(call: Call<DeleteGoodResponse>, t: Throwable) {
                    Toast.makeText(this@FoundGoodFragment.context, "Соединение разорвано или сервер не отвечает",  Toast.LENGTH_SHORT).show()
                    UtilUI.returnToMenu(view)
                }

                override fun onResponse(call: Call<DeleteGoodResponse>, response: Response<DeleteGoodResponse>) {
                    if (response.body() != null && response.body()?.success != null) {
                        Toast.makeText(this@FoundGoodFragment.context,response.body()?.success, Toast.LENGTH_SHORT).show()
                        UtilUI.returnToMenu(view)
                    } else {
                        if (response.body() != null && response.body()?.errors != null) {
                            Toast.makeText(this@FoundGoodFragment.context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                            UtilUI.returnToMenu(view)
                        }
                        Toast.makeText(this@FoundGoodFragment.context,"Неизвестная ошибка", Toast.LENGTH_SHORT).show()
                        UtilUI.returnToMenu(view)
                    }
                }
            })
        }
    }

    private fun initTextViews(view: View) {
        id = view.findViewById(R.id.good_id)
        id?.text = good?.id.toString()

        goodName = view.findViewById(R.id.goodName)
        goodName?.text = if (good?.name != null) good?.name.toString() else ""

        incomeDate = view.findViewById(R.id.goodIncomeDate)
        incomeDate?.text = if (good?.incomeDate != null) DateUtil.toHumanDate(good?.incomeDate as Date) else ""

        goodBarCode = view.findViewById(R.id.goodBarCode)
        goodBarCode?.text = good?.barCode

        sellPackaging = view.findViewById(R.id.sellPackaging)
        sellPackaging?.text = if (good?.sellPackaging != null) good?.sellPackaging.toString() else ""

        buyQuantity = view.findViewById(R.id.buyQuantity)
        buyQuantity?.text = if (good?.buyQuantity != null) good?.buyQuantity.toString() else ""

        stored = view.findViewById(R.id.stored)
        stored?.text = if (good?.stored != null) good?.stored.toString() else ""

        buyPrice = view.findViewById(R.id.buyPrice)
        buyPrice?.text = if (good?.buyPrice != null) good?.buyPrice.toString() else ""

        price = view.findViewById(R.id.price)
        price?.text = if (good?.price != null) good?.price.toString() else ""

        manufacturer = view.findViewById(R.id.manufacturer)
        manufacturer?.text = if (good?.manufacturer != null) good?.manufacturer.toString() else ""
    }
}