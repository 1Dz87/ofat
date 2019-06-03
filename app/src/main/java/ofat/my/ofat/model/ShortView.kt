package ofat.my.ofat.model

import android.content.res.ColorStateList
import android.os.Parcelable
import android.text.InputType
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.parcel.Parcelize
import ofat.my.ofat.R
import ofat.my.ofat.ui.main.FoundListFragment

@Parcelize
data class ShortView(
    var view: String = "",
    var id: Long = 0L,
    var barcode: String?,
    var className: String = ""
) : Parcelable {
    fun makeTextView(
        textView: TextView,
        index: Int,
        textColor: Int
    ) {
        fillTextView(textView, index, textColor)
        when (this.className) {
            Good::class.java.simpleName -> textView.setOnClickListener(FoundListFragment.onGoodClick(this))
            GoodsGroup::class.java.simpleName -> textView.setOnClickListener(FoundListFragment.onGoodGroupClick(this))
            Bookkeeper::class.java.simpleName -> textView.setOnClickListener(FoundListFragment.onBookkeeperClick(this))
        }
    }

    private fun fillTextView(textView: TextView, index: Int,
                             textColor: Int) {
        textView.text = this.view
        textView.textSize = 18F
        textView.isElegantTextHeight = true
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.setSingleLine(false)
        textView.setTextColor(ColorStateList.valueOf(textColor))
        textView.setBackgroundResource(R.color.colorPrimary)
        textView.id = index
        val lParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        lParams.setMargins(0, 0, 0, 3)
        textView.layoutParams = lParams
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0)
    }
}