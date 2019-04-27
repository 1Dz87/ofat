package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable

data class Document(
    var name: String?
    ) : Parcelable {

    constructor() : this("")

    constructor(parcel: Parcel) : this("") {
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }
}