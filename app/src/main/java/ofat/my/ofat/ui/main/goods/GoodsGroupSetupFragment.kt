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

class GoodsGroupSetupFragment : Fragment() {

    private lateinit var addGroup: Button

    private lateinit var editGroup: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goods_group_setup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setupHeader()
        initButtons()
        super.onActivityCreated(savedInstanceState)
    }

    private fun initButtons() {
        addGroup = view?.findViewById(R.id.addGroup)!!
        addGroup.setOnClickListener {
            view?.findNavController()?.navigate(R.id.addGroupFragment)
        }

        editGroup = view?.findViewById(R.id.editGroup)!!
        editGroup.setOnClickListener {
            view?.findNavController()?.navigate(R.id.editGroupFragment)
        }
    }

    private fun setupHeader() {
        (activity as MainActivity).setCurrentTitle("Работа с группами товаров")
    }
}
