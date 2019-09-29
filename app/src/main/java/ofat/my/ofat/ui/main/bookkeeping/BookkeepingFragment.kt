package ofat.my.ofat.ui.main.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R

class BookkeepingFragment : Fragment() {

    private lateinit var btCreate: Button

    private lateinit var btApply: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bookkeeping, container, false)
        (activity as MainActivity).setCurrentTitle("Бухгалтерия")
        initView(view)
        return view
    }

    private fun initView(view : View) {
        btCreate = view.findViewById(R.id.btCreateTemplate)
        btCreate.setOnClickListener { view.findNavController().navigate(R.id.bookkeeperCreateFragment) }

        btApply = view.findViewById(R.id.btApplyTemplate)
        btApply.setOnClickListener { view.findNavController().navigate(R.id.getBookkeeperFragment) }
    }
}
