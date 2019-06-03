package ofat.my.ofat.ui.main.bookkeeping


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
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import kotlinx.android.synthetic.main.main_activity.view.*
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.Util.ExtractUtil
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.CreateBookkeepersResponse
import ofat.my.ofat.api.response.GetPointsResponse
import ofat.my.ofat.model.Bookkeeper
import ofat.my.ofat.model.Point
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.FoundListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookkeeperCreateFragment : Fragment() {

    private lateinit var bookkeeperName: TextInputEditText

    private lateinit var treeView: AndroidTreeView

    private lateinit var createBt: FloatingActionButton

    private lateinit var progress: ProgressBar

    private lateinit var viewModel: FoundListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookkeeper_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        (activity as MainActivity).setCurrentTitle(R.string.search_result)
        activity?.let {
            viewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
        }
    }

    private fun initView() {
        bookkeeperName = view?.findViewById(R.id.book_name_edit_text)!!
        progress = view?.findViewById(R.id.progressBarBK)!!
        createBt = view?.findViewById(R.id.createBookkeeper)!!
        createBt.setOnClickListener { onCreateClick() }
        initPointTree()
    }

    private fun onCreateClick() {
        val bookkeeper = getBookkeepersData()
        if (bookkeeper != null) {
            val call = OfatApplication.bookkeeperApi?.createBookkeeper(bookkeeper)
            UtilUI.showProgress(progress)
            call?.enqueue(object : Callback<CreateBookkeepersResponse> {
                override fun onFailure(call: Call<CreateBookkeepersResponse>, t: Throwable) {
                    UtilUI.showProgress(progress)
                    Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<CreateBookkeepersResponse>,
                    response: Response<CreateBookkeepersResponse>
                ) {
                    UtilUI.showProgress(progress)
                    if (response.body() != null && response.body()?.success != null) {
                        bookkeeper.id = response.body()?.success as Long
                        viewModel.foundList.value?.add(ShortView(bookkeeper.name, bookkeeper.id!!, null, Bookkeeper::class.java.simpleName))
                        view?.findNavController()?.navigateUp()
                        Toast.makeText(context, "Бухгалтерия создана успешно", Toast.LENGTH_SHORT).show()
                    } else if (response.body() != null && response.body()?.errors != null) {
                        Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
    }

    private fun getBookkeepersData(): Bookkeeper? {
        if (UtilUI.checkTextFields(arrayOf(bookkeeperName))) {
            val result = Bookkeeper()
            result.name = ExtractUtil.v(bookkeeperName) as String
            if (CollectionUtils.isNotEmpty(treeView.selected)) {
                result.points = extractSelectedPoints()
            }
            return result
        }
        return null
    }

    private fun extractSelectedPoints(): MutableList<Point> {
        val result = mutableListOf<Point>()
        treeView.selected.forEach {
            result.add(it.value as Point)
        }
        return result
    }

    private fun initPointTree() {
        val treeRoot = TreeNode.root()
        val parent = TreeNode("Точки")
        val pointList = getPoints()
        pointList.forEach {
            val node = TreeNode(it)
            node.setClickListener { nod, _ -> nod.isSelected = !nod.isSelected }
            parent.addChild(node)
        }
        treeRoot.addChild(parent)
        treeView = AndroidTreeView(activity!!, treeRoot)
        view?.container?.addView(treeView.view)
    }

    private fun getPoints(): List<Point> {
        val call = OfatApplication.pointsApi?.getPoints()
        var result = listOf<Point>()
        call?.enqueue(object : Callback<GetPointsResponse> {
            override fun onFailure(call: Call<GetPointsResponse>, t: Throwable) {
                Toast.makeText(context, OfatConstants.ON_FAILURE_ERROR, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<GetPointsResponse>,
                response: Response<GetPointsResponse>
            ) {
                if (response.body() != null && response.body()?.success != null) {
                    result = response.body()?.success as List<Point>
                } else if (response.body() != null && response.body()?.errors != null) {
                    Toast.makeText(context, response.body()?.errors, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, OfatConstants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                }
            }

        })
        return result
    }
}
