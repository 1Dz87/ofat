package ofat.my.ofat.ui.main.goods

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.WebUtil
import ofat.my.ofat.api.response.GetGoodShortViewResponse
import ofat.my.ofat.api.response.GoodsGroupSVResponse
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class GoodsGroupSetupFragment : Fragment() {

    private lateinit var addGroup: Button

    private lateinit var editGroup: Button

    private lateinit var foundViewModel: FoundListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_goods_group_setup, container, false)
        foundViewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupHeader()
        initButtons()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initButtons() {
        addGroup = view?.findViewById(R.id.addGroup)!!
        addGroup.setOnClickListener {
            view?.findNavController()?.navigate(R.id.addGroupFragment)
        }

        editGroup = view?.findViewById(R.id.editGroup)!!
        editGroup.setOnClickListener {
            onEditClick()
            //view?.findNavController()?.navigate(R.id.editGroupFragment)
        }
    }

    private fun onEditClick() {
        val call = OfatApplication.goodsGroupApi?.getGoodsGroupsSV()
        call?.enqueue(object : Callback<GoodsGroupSVResponse> {
            @SuppressLint("ThrowableNotAtBeginning", "TimberExceptionLogging")
            override fun onFailure(call: Call<GoodsGroupSVResponse>, t: Throwable) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show()
                Timber.e(t, t.message, t.cause)
            }

            override fun onResponse(
                call: Call<GoodsGroupSVResponse>,
                response: Response<GoodsGroupSVResponse>
            ) {
                view?.findNavController()?.popBackStack()
                if (response.body()?.success != null) {
                    foundViewModel.foundList.value = response.body()?.success as MutableCollection<ShortView>
                    view?.findNavController()?.navigate(R.id.foundListFragment)
                } else {
                    Toast.makeText(context, WebUtil.checkUnauthCode(response, "Группа не найдена.", null), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupHeader() {
        (activity as MainActivity).setCurrentTitle("Работа с группами товаров")
    }
}
