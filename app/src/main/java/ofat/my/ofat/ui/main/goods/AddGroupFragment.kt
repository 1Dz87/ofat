package ofat.my.ofat.ui.main.goods

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.Util.WebUtil
import ofat.my.ofat.api.response.GoodsGroupResponse
import ofat.my.ofat.model.GoodsGroup
import ofat.my.ofat.persistence.OfatDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGroupFragment : Fragment() {

    private lateinit var groupName: TextInputEditText

    private lateinit var btAdd: FloatingActionButton

    private lateinit var progress: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_group, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initButtons()
        initEditText()
        initProgress()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initProgress() {
        progress = view?.findViewById(R.id.progressBarAddGroup)!!
    }

    private fun initButtons() {
        btAdd = view?.findViewById(R.id.addGroupFAB)!!
        btAdd.setOnClickListener { onAddBtClick() }
    }

    private fun initEditText() {
        groupName = view?.findViewById(R.id.good_group_name_edit_text)!!
    }

    private fun onAddBtClick() {
        if (UtilUI.checkTextFields(arrayOf(groupName))) {
            val group = makeGroup()
            val call = OfatApplication.goodsGroupApi?.createGoodsGroup(group)
            UtilUI.showProgress(progress)
            call?.enqueue(object : Callback<GoodsGroupResponse>{
                override fun onFailure(call: Call<GoodsGroupResponse>, t: Throwable) {
                    UtilUI.showProgress(progress)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<GoodsGroupResponse>, response: Response<GoodsGroupResponse>) {
                    UtilUI.showProgress(progress)
                    if (response.body() != null && response.body()?.success != null) {
                        val savedGroup = response.body()?.success
                        OfatDatabase.getInstance(this@AddGroupFragment.context!!).goodsGroupDao().save(savedGroup!!)
                        Toast.makeText(this@AddGroupFragment.context, "Группа ${savedGroup.name} создана успешно.", Toast.LENGTH_SHORT).show()
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

    private fun makeGroup() : GoodsGroup {
        val name = ExtractUtil.v(groupName)!!
        return GoodsGroup(null, name, listOf())
    }
}
