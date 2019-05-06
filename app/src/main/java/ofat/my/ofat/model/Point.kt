package ofat.my.ofat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Point(val id: Long, val name: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}