package ofat.my.ofat.ui.main.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.MainActivity

import ofat.my.ofat.R

class BookkeepingListFragment : Fragment() {

    private lateinit var table: TableLayout

    private lateinit var addBt: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bookkeeping, container, false)
        (activity as MainActivity).setCurrentTitle("Бухгалтерия")
        initView(view)
        return view
    }

    private fun initView(view : View) {
        table = view.findViewById(R.id.bookkeepersTable)
        addBt = view.findViewById(R.id.addBookkeeper)
        addBt.setOnClickListener {
            view.findNavController().navigate(R.id.bookkeeperCreateFragment)
        }
    }
}
