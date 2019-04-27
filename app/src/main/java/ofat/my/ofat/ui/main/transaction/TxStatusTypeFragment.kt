package ofat.my.ofat.ui.main.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ofat.my.ofat.R
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.TransactionType
import android.widget.Toast
import androidx.navigation.findNavController
import ofat.my.ofat.MainActivity
import ofat.my.ofat.Util.StringUtils


class TxStatusTypeFragment : Fragment(), View.OnClickListener {

    private var good: Good? = null

    private var type_SR: Spinner? = null

    private var accepted_bt: RadioButton? = null

    private var notAccepted_bt: RadioButton? = null

    private var delayed_bt: RadioButton? = null

    private var btForward: FloatingActionButton? = null

    private var selectedStatus: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tx_status_type, container, false)
        if (good == null) {
            good = arguments?.getParcelable("good")!!
        }
        initView(view)
        (activity as MainActivity).setCurrentTitle(R.string.transaction_title)
        return view
    }

    private fun initView(view: View?) {
        type_SR = view?.findViewById(R.id.tx_type_spinner)
        type_SR?.adapter =
            ArrayAdapter<TransactionType>(activity!!, R.layout.spinner_item, R.id.spinner_item_tv, TransactionType.values())

        accepted_bt = view?.findViewById(R.id.accepted)
        accepted_bt?.setOnClickListener(this)

        notAccepted_bt = view?.findViewById(R.id.not_accepted)
        notAccepted_bt?.setOnClickListener(this)

        delayed_bt = view?.findViewById(R.id.delayed)
        delayed_bt?.setOnClickListener(this)

        btForward = view?.findViewById(R.id.bt_tx_forward)
        btForward?.setOnClickListener {
            if (checkFields()) {
                val bundle = Bundle()
                bundle.putParcelable("good", good)
                bundle.putInt("type", type_SR!!.selectedItemPosition)
                bundle.putInt("status", selectedStatus!!)
                view?.findNavController()?.popBackStack()
                view?.findNavController()?.navigate(R.id.transactionFragment, bundle)
            } else {
                Toast.makeText(context, "Выберите тип и статус транзакции", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFields() : Boolean {
        return StringUtils.isNotBlanc(type_SR!!.selectedItem.toString()) && selectedStatus != null
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.accepted -> {
                selectedStatus = 0
            }

            R.id.not_accepted -> {
                selectedStatus = 1
            }

            R.id.delayed -> selectedStatus = 2
        }
    }
}
