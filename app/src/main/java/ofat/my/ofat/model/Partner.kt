package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable

data class Partner(
    var name: String?,
    var type: PartnerType?,
    var documents: MutableList<Document>?,
    var comment: String?
    ) : Parcelable{

    constructor() : this("", null, null, "")

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(PartnerType::class.java.classLoader) as PartnerType,
        parcel.readList(mutableListOf<Document>(), MutableList::class.java.classLoader) as MutableList<Document>,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(type)
        parcel.writeList(documents)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Partner> {
        override fun createFromParcel(parcel: Parcel): Partner {
            return Partner(parcel)
        }

        override fun newArray(size: Int): Array<Partner?> {
            return arrayOfNulls(size)
        }
    }
}