package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable

data class ShortView(    var view: String = "",
                         var id: Long = 0L,
                         var barcode: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(view)
        parcel.writeLong(id)
        parcel.writeString(view)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShortView> {
        override fun createFromParcel(parcel: Parcel): ShortView {
            return ShortView(parcel)
        }

        override fun newArray(size: Int): Array<ShortView?> {
            return arrayOfNulls(size)
        }
    }
}