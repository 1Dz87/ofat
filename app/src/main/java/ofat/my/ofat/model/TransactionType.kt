package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable

enum class TransactionType(private val displayValue: String?) : Parcelable {

    EMPTY(""),
    IN("Покупка"),
    OUT("Продажа");

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionType> {
        override fun createFromParcel(parcel: Parcel): TransactionType {
            return if (parcel.readString().equals("Продажа")) {
                OUT
            } else IN
        }

        override fun newArray(size: Int): Array<TransactionType?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return displayValue!!
    }
}