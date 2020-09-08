package ofat.my.ofat.ui.main.goods


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.Util.*
import ofat.my.ofat.api.response.DeleteGoodResponse
import ofat.my.ofat.api.response.GoodsGroupResponse
import ofat.my.ofat.model.GoodsGroup
import ofat.my.ofat.persistence.OfatDatabase
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditGoodGroupFragment : Fragment() {

    private lateinit var groupName: TextInputEditText

    private lateinit var btSave: FloatingActionButton

    private lateinit var btDel: FloatingActionButton

    private lateinit var btBack: FloatingActionButton

    private lateinit var progress: ProgressBar

    private var groupId: Long? = null

    private lateinit var group: GoodsGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_good_group, container, false)
        groupId = arguments?.getLong("groupId")
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initButtons()
        initEditText()
        initProgress()
        initData()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initData() {
        if (groupId != null) {
            val call = OfatApplication.goodsGroupApi?.getById(groupId!!)
            UtilUI.showProgress(progress)
            call?.enqueue(object : Callback<GoodsGroupResponse> {
                override fun onFailure(call: Call<GoodsGroupResponse>, t: Throwable) {
                    UtilUI.showProgress(progress)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<GoodsGroupResponse>, response: Response<GoodsGroupResponse>) {
                    UtilUI.showProgress(progress)
                    if (response.body() != null && response.body()?.success != null) {
                        group = response.body()?.success as GoodsGroup
                        groupName.setText(group.name)
                    } else {
                        Toast.makeText(context, WebUtil.checkUnauthCode(response, OfatConstants.UNKNOWN_ERROR, null), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun initProgress() {
        progress = view?.findViewById(R.id.progressBarEditGroup)!!
    }

    private fun initButtons() {
        btSave = view?.findViewById(R.id.saveGroupFAB)!!
        btSave.setOnClickListener { onSaveBtClick() }

        btDel = view?.findViewById(R.id.delGroupFAB)!!
        btDel.setOnClickListener { onDelClick() }

        btBack = view?.findViewById(R.id.backGroupFAB)!!
        btBack.setOnClickListener { onBackClick() }
    }

    private fun initEditText() {
        groupName = view?.findViewById(R.id.good_group_name_edit_text)!!
    }

    private fun onBackClick() {
        view?.findNavController()?.navigateUp()
    }

    private fun onDelClick() {
        if (groupId != null) {
            val call = OfatApplication.goodsGroupApi?.deleteGroup(groupId!!)
            UtilUI.showProgress(progress)
            call?.enqueue(object : Callback<DeleteGoodResponse> {
                override fun onFailure(call: Call<DeleteGoodResponse>, t: Throwable) {
                    UtilUI.showProgress(progress)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<DeleteGoodResponse>, response: Response<DeleteGoodResponse>) {
                    UtilUI.showProgress(progress)
                    if (response.body() != null && response.body()?.success != null) {
                        Toast.makeText(context, response.body()?.success, Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigateUp()
                    } else {
                        Toast.makeText(context, WebUtil.checkUnauthCode(response, OfatConstants.UNKNOWN_ERROR, null), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun onSaveBtClick() {
        if (UtilUI.checkTextFields(arrayOf(groupName))) {
            updateGroupData()
            val call = OfatApplication.goodsGroupApi?.updateGoodsGroup(group)
            UtilUI.showProgress(progress)
            call?.enqueue(object : Callback<GoodsGroupResponse> {
                override fun onFailure(call: Call<GoodsGroupResponse>, t: Throwable) {
                    UtilUI.showProgress(progress)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<GoodsGroupResponse>, response: Response<GoodsGroupResponse>) {
                    UtilUI.showProgress(progress)
                    if (response.body() != null && response.body()?.success != null) {
                        Toast.makeText(this@EditGoodGroupFragment.context, "Группа ${group.name} обновлена успешно.", Toast.LENGTH_SHORT).show()
                        val navController = view?.findNavController()
                        navController?.popBackStack(R.id.menuFragment, false)
                        navController?.navigate(R.id.menuFragment)
                    } else {
                        Toast.makeText(context, WebUtil.checkUnauthCode(response, OfatConstants.UNKNOWN_ERROR, null), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun updateGroupData() {
        val name = ExtractUtil.v(groupName)!!
        group.name = name
    }

}
