package ofat.my.ofat.ui.main.bookkeeping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.MainActivity
import ofat.my.ofat.R
import ofat.my.ofat.databinding.FragmentGetBookkeeperBinding
import ofat.my.ofat.model.QueryBookResponse
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.FoundListViewModel

class GetBookkeeperFragment : Fragment() {

    private lateinit var viewModel: FoundListViewModel

    private lateinit var bookkeepersViewModel: BookkeepersViewModel

    private lateinit var response: QueryBookResponse

    private lateinit var btBack: FloatingActionButton

    private lateinit var btTxList: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        response = arguments?.getParcelable<QueryBookResponse>("queryBookResponse")!!
        viewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
        bookkeepersViewModel = ViewModelProviders.of(activity!!).get(BookkeepersViewModel::class.java)
        val binding: FragmentGetBookkeeperBinding = DataBindingUtil
            .inflate(this.layoutInflater, R.layout.fragment_get_bookkeeper, container, false)
        binding.lifecycleOwner = this@GetBookkeeperFragment
        binding.response = response
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        bookkeepersViewModel.queryResponse.value = response
        (activity as MainActivity).setCurrentTitle("Результат запроса")
        initView()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initView() {
        btBack = view?.findViewById(R.id.backQueryResult)!!
        btBack.setOnClickListener { view?.findNavController()?.navigateUp() }

        btTxList = view?.findViewById(R.id.txList)!!
        btTxList.setOnClickListener {
            val txs = response.transactions
            viewModel.foundList.value = txs as MutableCollection<ShortView> ?: mutableListOf()
            view?.findNavController()?.navigate(R.id.foundListFragment)
        }
    }
}
