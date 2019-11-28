package ofat.my.ofat.ui.main.transaction


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ofat.my.ofat.MainActivity
import ofat.my.ofat.R
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.databinding.FragmentViewTxBinding
import ofat.my.ofat.ui.main.bookkeeping.BookkeepersViewModel

class ViewTxFragment : Fragment() {

    private lateinit var selectedTxId: String

    private lateinit var viewModel: BookkeepersViewModel

    private lateinit var progressBarViewTx: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedTxId = arguments?.getString("selectedTx")!!
        viewModel = ViewModelProviders.of(activity!!).get(BookkeepersViewModel::class.java)
        val binding: FragmentViewTxBinding = DataBindingUtil
            .inflate(this.layoutInflater, R.layout.fragment_view_tx, container, false)
        progressBarViewTx = binding.root.findViewById(R.id.progressBarViewTx)
        binding.lifecycleOwner = this@ViewTxFragment
        viewModel.getSelectedTx(selectedTxId.toLong()) { value -> binding.tx = value}
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        (activity as MainActivity).setCurrentTitle("Детали транзакции")
        super.onActivityCreated(savedInstanceState)
    }


}
