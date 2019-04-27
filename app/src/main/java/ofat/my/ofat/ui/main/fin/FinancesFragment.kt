package ofat.my.ofat.ui.main.fin

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ofat.my.ofat.R

class FinancesFragment : Fragment() {

    companion object {
        fun newInstance() = FinancesFragment()
    }

    private lateinit var viewModel: FinancesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.finances_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FinancesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
