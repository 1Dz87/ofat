package ofat.my.ofat.ui.main

import android.content.res.ColorStateList
import android.content.res.Resources
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.model.ShortView

class FoundListFragment : Fragment() {

    private lateinit var viewModel: FoundListViewModel

   // private lateinit var viewModel: MainViewModel

    private lateinit var table: TableLayout

    var fromFast = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.found_list_fragment, container, false)
        fromFast = arguments?.getBoolean("fromFast") ?: false
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
                    makeTextView(textView, currElement, index)
                    row.layoutParams =
                        TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                    row.addView(textView)
                    table.addView(row)
                }
            }
        })
    }

    private fun makeTextView(textView : TextView, currElement : ShortView, index: Int) {
        textView.text = currElement.view
        textView.textSize = 18F
        textView.isElegantTextHeight = true
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.setSingleLine(false)
        textView.setTextColor(ColorStateList.valueOf(resources.getColor(R.color.white)))
        textView.setBackgroundResource(R.color.colorPrimary)
        textView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("goodId", currElement.id.toString())
            bundle.putBoolean("fromFast", fromFast)
            view?.findNavController()?.navigate(R.id.foundGoodFragment, bundle)
        }
        textView.id = index
        val lParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        lParams.setMargins(0, 0, 0, 3)
        textView.layoutParams = lParams
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0)
    }
}
