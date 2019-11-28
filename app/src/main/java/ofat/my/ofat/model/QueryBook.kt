package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.HashMap

data class QueryBookResponse(
    val debit: Double?,
    val credit: Double?,
    val balance: Double?,
    val transactions: List<ShortView>?,
    val commonInfo: Map<String, Any>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(List::class.java.classLoader) as? List<ShortView>,
        readMap(parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(debit)
        parcel.writeValue(credit)
        parcel.writeValue(balance)
        parcel.writeValue(transactions)
        writeMap(parcel, commonInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QueryBookResponse> {
        override fun createFromParcel(parcel: Parcel): QueryBookResponse {
            return QueryBookResponse(parcel)
        }

        override fun newArray(size: Int): Array<QueryBookResponse?> {
            return arrayOfNulls(size)
        }

        private fun readMap(parcel: Parcel): Map<String, Any>? {
            val size = parcel.readInt()
            val result = HashMap<String, Any>(size)
            var i  = 0
            while (i < size) {
                result[parcel.readString()] = parcel.readValue(Any::class.java.classLoader)
                i++
            }
            return result
        }

        private fun writeMap(parcel: Parcel, commonInfo: Map<String, Any>?) {
            if (commonInfo != null) {
                parcel.writeInt(commonInfo.size)
                commonInfo.forEach{
                    parcel.writeString(it.key)
                    parcel.writeValue(it.value)
                }
            }
        }
    }
}

@Parcelize
data class QueryBookRequest(
    var id: Long?,
    var good: Long?,
    var group: Long?,
    var user: Long?,
    var point: Long?,
    var dateFrom: Date?,
    var dateTo: Date?
    ) : Parcelable