package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable

enum class PartnerType (displayValue: String?) : Parcelable {

    LEGAL("Юр. лицо"),
    PRIVATE("Физ. лицо");

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PartnerType> {
        override fun createFromParcel(parcel: Parcel): PartnerType {
            return if (parcel.readString().equals("Юр. лицо")) {
                PartnerType.LEGAL
            } else PartnerType.PRIVATE
        }

        override fun newArray(size: Int): Array<PartnerType?> {
            return arrayOfNulls(size)
        }
    }
}
