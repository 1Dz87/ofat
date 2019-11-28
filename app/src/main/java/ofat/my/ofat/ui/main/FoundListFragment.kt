package ofat.my.ofat.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.bookkeeping.BookkeepersViewModel

class FoundListFragment : Fragment() {

    private lateinit var viewModel: FoundListViewModel

    private lateinit var table: TableLayout

    companion object OnClickListeners {
        var fromFast = false
        var fromBk = false
        lateinit var bookkeepersViewModel: BookkeepersViewModel

        fun onGoodClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                val bundle = Bundle()
                bundle.putString("goodId", currElement.id.toString())
                bundle.putBoolean("fromFast", fromFast)
                if (fromBk) {
                    bookkeepersViewModel.queryGood.value = currElement
                    it.findNavController().navigate(R.id.bookkeeperCreateFragment, bundle)
                } else {
                    it.findNavController().navigate(R.id.foundGoodFragment, bundle)
                }
            }
        }

        fun onGoodGroupClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                val bundle = Bundle()
                bundle.putLong("groupId", currElement.id)
                it.findNavController().navigate(R.id.editGroupFragment, bundle)
            }
        }

        fun onUserClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                bookkeepersViewModel.queryUser.value = currElement
                it.findNavController().navigateUp()
            }
        }

        fun onTransactionClick(currElement: ShortView) : View.OnClickListener {
            return View.OnClickListener {
                val bundle = Bundle()
                bundle.putString("selectedTx", currElement.id.toString())
                it.findNavController().navigate(R.id.viewTxFragment, bundle)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.found_list_fragment, container, false)
        fromFast = arguments?.getBoolean("fromFast") ?: false
        fromBk = arguments?.getBoolean("fromBk") ?: false
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.search_result)
        activity?.let {
            viewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
            bookkeepersViewModel = ViewModelProviders.of(activity!!).get(BookkeepersViewModel::class.java)
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
    }
}
