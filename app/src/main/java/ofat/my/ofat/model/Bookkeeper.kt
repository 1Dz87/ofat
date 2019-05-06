package ofat.my.ofat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Bookkeeper (
    var id : Long?,
    var name : String,
    var transactions : MutableList<Transaction> = Collections.emptyList(),
    var points: MutableList<Point> = Collections.emptyList()
): Parcelable {
    constructor() : this(null, "")
}