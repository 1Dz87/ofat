package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Transaction(
    var id: Long?,
    var good: Good?,
    var price: Double?,
    var status: TransactionStatus?,
    var date: Date?,
    var quantity: Double?,
    var type: TransactionType?,
    var comment: String?,
    var user: User?,
    var partner: Partner?
) : Parcelable {

    constructor() : this (null, null, null, null, null, null, null, "", null, null)

    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(Good::class.java.classLoader),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? TransactionStatus,
        parcel.readValue(Date::class.java.classLoader) as? Date,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readParcelable(TransactionType::class.java.classLoader) as TransactionType,
        parcel.readString(),
        parcel.readParcelable<User>(User::class.java.classLoader) as User,
        parcel.readParcelable(Partner::class.java.classLoader) as Partner
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(good, flags)
        parcel.writeValue(price)
        parcel.writeValue(status)
        parcel.writeValue(date)
        parcel.writeDouble(quantity!!)
        parcel.writeValue(type)
        parcel.writeValue(partner)
        parcel.writeParcelable(user, flags)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}