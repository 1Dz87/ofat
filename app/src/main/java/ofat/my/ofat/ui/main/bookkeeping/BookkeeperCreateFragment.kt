package ofat.my.ofat.ui.main.bookkeeping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.main_activity.*
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.GetUsersListResponse
import ofat.my.ofat.api.response.GoodsGroupNamesResponse
import ofat.my.ofat.api.response.QueryBookResponseObj
import ofat.my.ofat.databinding.FragmentBookkeeperCreateBinding
import ofat.my.ofat.model.GoodsGroup
import ofat.my.ofat.model.QueryBookRequest
import ofat.my.ofat.model.QueryBookResponse
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class BookkeeperCreateFragment : Fragment() {

    private lateinit var groupSpinner: Spinner

    private lateinit var progress: ProgressBar

    private lateinit var btGood: TextInputEditText

    private lateinit var btDateFrom: TextInputEditText

    private lateinit var btDateTo: TextInputEditText

    private lateinit var btUser: TextInputEditText

    private lateinit var viewModel: FoundListViewModel

    private lateinit var bookkeepersViewModel: BookkeepersViewModel

    private lateinit var btCreateTemplate: Button

    private lateinit var btBackFromTemplate: Button

    private lateinit var viewGroupBk: ViewGroup

    private var goodId: Long? = null

    private var userId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookkeeper_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
        bookkeepersViewModel = ViewModelProviders.of(activity!!).get(BookkeepersViewModel::class.java)
        val binding: FragmentBookkeeperCreateBinding = DataBindingUtil.inflate(this.layoutInflater, R.layout.fragment_bookkeeper_create, container, false)
        binding.viewModel = bookkeepersViewModel
        setViewModelLsteners()
        initView()
        (activity as MainActivity).setCurrentTitle(R.string.create_template)
    }

    private fun setViewModelLsteners() {
        bookkeepersViewModel.queryGood.observe(this, Observer {
            goodId = it.id
        })
        bookkeepersViewModel.queryUser.observe(this, Observer {
            userId = it.id
        })
    }

    private fun initView() {
        progress = view?.findViewById(R.id.progressBarBookkeepers)!!

        groupSpinner = view?.findViewById(R.id.goods_group_bk_spinner)!!
        initGoodsGroupsNamesList()

        btGood = view?.findViewById(R.id.queryGoodET)!!
        UtilUI.fieldAsButton(btGood) {
            view?.findNavController()?.navigate(R.id.findBkGoodCamFragment) }

        btDateFrom = view?.findViewById(R.id.queryDateETF)!!
        UtilUI.setDateToField(btDateFrom, activity!!)

        btDateTo = view?.findViewById(R.id.queryDateETT)!!
        UtilUI.setDateToField(btDateTo, activity!!)

        btUser = view?.findViewById(R.id.queryUserET)!!
        UtilUI.fieldAsButton(btUser) {
            getUsersList()
            view?.findNavController()?.navigate(R.id.foundListFragment) }

        btCreateTemplate = view?.findViewById(R.id.btCreateTemplate)!!
        btCreateTemplate.setOnClickListener {
            onCreateClick()
        }

        btBackFromTemplate = view?.findViewById(R.id.btBackFromTemplate)!!
        btBackFromTemplate.setOnClickListener {
            this.goodId = null
            this.userId = null
            view?.findNavController()?.navigateUp()
        }
    }

    private fun makeQuery(): QueryBookRequest {
        val group = if ((groupSpinner.selectedItem as GoodsGroup) != null
            || (groupSpinner.selectedItem as GoodsGroup).id != null) {
                (groupSpinner.selectedItem as GoodsGroup).id
        } else null
        val dateAsDateFrom = ExtractUtil.v(btDateFrom)
        val dateAsDateTo = ExtractUtil.v(btDateTo)
        val dateFrom = if (dateAsDateFrom != null) SimpleDateFormat("dd.MM.yyyy").parse(dateAsDateFrom) else null
        val dateTo = if (dateAsDateTo != null) SimpleDateFormat("dd.MM.yyyy").parse(dateAsDateTo) else null
        return QueryBookRequest(null, goodId, group, userId, null, dateFrom, dateTo) //TODO: 5-й аргумент - это Point, добавить после реализации
    }

    private fun getUsersList() {
        val call = OfatApplication.userApi?.getUsers()
        UtilUI.showProgress(progress)
        call?.enqueue(object : Callback<GetUsersListResponse>{
            override fun onFailure(call: Call<GetUsersListResponse>, t: Throwable) {
                UtilUI.showProgress(progress)
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GetUsersListResponse>, response: Response<GetUsersListResponse>) {
                UtilUI.showProgress(progress)
                if (response.body() != null && response.body()?.success != null) {
                    viewModel.foundList.value = response.body()?.success as MutableList<ShortView>
                } else if (response.body() != null && response.body()?.errors != null) {
                    Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initGoodsGroupsNamesList() {
        val call = OfatApplication.goodsGroupApi?.getGoodsGroupsNames()
        UtilUI.showProgress(progress)
        call?.enqueue(object : Callback<GoodsGroupNamesResponse>{
            override fun onFailure(call: Call<GoodsGroupNamesResponse>, t: Throwable) {
                UtilUI.showProgress(progress)
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GoodsGroupNamesResponse>, response: Response<GoodsGroupNamesResponse>) {
                UtilUI.showProgress(progress)
                if (response.body() != null && response.body()?.success != null) {
                    val groups = response.body()?.success as List<GoodsGroup>
                    groupSpinner.adapter =
                        ArrayAdapter<GoodsGroup>(activity!!, R.layout.spinner_item, R.id.spinner_item_tv, groups)
                } else if (response.body() != null && response.body()?.errors != null) {
                    Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun onCreateClick() {
        val query: QueryBookRequest = makeQuery()
        val call = OfatApplication.templatesApi?.queryTemplate(query)
        UtilUI.showProgress(progress)
        call?.enqueue(object : Callback<QueryBookResponseObj>{
            override fun onFailure(call: Call<QueryBookResponseObj>, t: Throwable) {
                UtilUI.showProgress(progress)
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<QueryBookResponseObj>, response: Response<QueryBookResponseObj>) {
                UtilUI.showProgress(progress)
                if (response.body() != null && response.body()?.success != null) {
                    val resp = response.body()?.success as QueryBookResponse
                    val bundle = Bundle()
                    bundle.putParcelable("queryBookResponse", resp)
                    view?.findNavController()?.navigate(R.id.getBookkeeperFragment, bundle)
                } else if (response.body() != null && response.body()?.errors != null) {
                    Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
