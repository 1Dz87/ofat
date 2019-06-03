package ofat.my.ofat.ui.main

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.model.ShortView

class FoundListFragment : Fragment() {

    private lateinit var viewModel: FoundListViewModel

    private lateinit var table: TableLayout

    private var bookkeeper = false

    companion object OnClickListeners {
        var fromFast = false

        fun onGoodClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                val bundle = Bundle()
                bundle.putString("goodId", currElement.id.toString())
                bundle.putBoolean("fromFast", fromFast)
                it.findNavController().navigate(R.id.foundGoodFragment, bundle)
            }
        }

        fun onGoodGroupClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                val bundle = Bundle()
                bundle.putLong("groupId", currElement.id)
                it.findNavController().navigate(R.id.editGroupFragment, bundle)
            }
        }

        fun onBookkeeperClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                val bundle = Bundle()
                bundle.putLong("bookkeeperId", currElement.id)
                it.findNavController().navigate(R.id.getBookkeeperFragment, bundle)
            }
        }

        fun addCreateBookkeeperBt(view: View) {
            val btLayout: LinearLayout = view.findViewById(R.id.additionalLayout)!!
            val btCreate = FloatingActionButton(view.context)
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(32, 32, 32, 32)
            layoutParams.gravity = Gravity.END
            btCreate.setImageResource(R.drawable.ic_control_point_black_24dp)
            btCreate.layoutParams = layoutParams
            btCreate.backgroundTintList = ColorStateList.valueOf(view.resources.getColor(R.color.colorPrimary))
            btCreate.setOnClickListener {
                view.findNavController().navigate(R.id.bookkeeperCreateFragment)
            }
            btLayout.addView(btCreate)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.found_list_fragment, container, false)
        fromFast = arguments?.getBoolean("fromFast") ?: false
        bookkeeper = arguments?.getBoolean("bookkeeper") ?: false
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.search_result)
        activity?.let {
            viewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
            init(this.view!!)
        }
    }

    private fun init(view: View) {
        table = view.findViewById(R.id.foundTable)
        viewModel.foundList.observe(this, Observer {
            if (CollectionUtils.isNotEmpty(it)) {
                for (index in it!!.indices) {
                    val row = TableRow(view.context)
                    val textView = TextView(view.context)
                    val currElement = it.elementAt(index)
                    currElement.makeTextView(textView, index, resources.getColor(R.color.white))
                    row.layoutParams =
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                    row.addView(textView)
                    table.addView(row)
                }
            }
        })
        if (bookkeeper) {
            addCreateBookkeeperBt(view)
        }
    }
}
