package ofat.my.ofat.ui.main.goods


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R

class SelectionBeforeGoodFragment : Fragment() {

    private lateinit var btGoods: Button

    private lateinit var btGroups: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selection_before_good, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupHeader()
        initButtons()
        super.onActivityCreated(savedInstanceState)
    }

    private fun setupHeader() {
        (activity as MainActivity).setCurrentTitle("Работа с товаром")
    }

    private fun initButtons() {
        btGoods = view?.findViewById(R.id.btGoodsSelection)!!
        btGoods.setOnClickListener {
            view?.findNavController()?.navigate(R.id.goodsFragment)
        }

        btGroups = view?.findViewById(R.id.btGroups)!!
        btGroups.setOnClickListener {
            view?.findNavController()?.navigate(R.id.groupSetupFragment)
        }
    }


}
