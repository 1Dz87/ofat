package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = GoodsGroup::class,
            parentColumns = ["id"],
    childColumns = ["f_group"], onUpdate = CASCADE)]
)
data class Good(
    @PrimaryKey var id: Long?,
    @ColumnInfo(name = "f_name") var name: String?,
    @ColumnInfo(name = "f_barcode") var barCode: String?,
    @ColumnInfo(name = "f_sell_packaging") var sellPackaging: String?,
    @ColumnInfo(name = "f_discount") var discount: Double?,
    @ColumnInfo(name = "f_quantity") var buyQuantity: Double?,
    @ColumnInfo(name = "f_sell_quantity") var sellQuantity: Double?,
    @ColumnInfo(name = "f_stored") var stored: Int?,
    @ColumnInfo(name = "f_manufacturer") var manufacturer: String?,
    @ColumnInfo(name = "f_price") var price: Double?,
    @ColumnInfo(name = "f_buy_price") var buyPrice: Double?,
    @ColumnInfo(name = "f_incomeDate") var incomeDate: Date?,
    @ColumnInfo(name = "f_status") var status: GoodStatus?,
    @ColumnInfo(name = "f_goodComments") var goodComments: String?,
    @ColumnInfo(name = "f_points") var points: MutableList<Long>?,
    @ColumnInfo(name = "f_providers") var providers: MutableList<Long>?,
    @ColumnInfo(name = "f_group")var group: GoodsGroup? ): Parcelable {

    constructor() : this (null, "", "", "", 0.0, 0.0, 0.0, 0, "", 0.0, 0.0, null, null, "", Collections.emptyList(), Collections.emptyList(), null)

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Date::class.java.classLoader) as? Date,
        parcel.readValue(GoodStatus::class.java.classLoader) as? GoodStatus,
        parcel.readString(),
        parcel.createLongArray()!!.toMutableList(),
        parcel.createLongArray()!!.toMutableList(),
        parcel.readTypedObject(GoodsGroup.GroupCREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(barCode)
        parcel.writeString(sellPackaging)
        parcel.writeValue(discount)
        parcel.writeValue(buyQuantity)
        parcel.writeValue(sellQuantity)
        parcel.writeValue(stored)
        parcel.writeString(manufacturer)
        parcel.writeValue(price)
        parcel.writeValue(buyPrice)
        parcel.writeValue(incomeDate)
        parcel.writeValue(status)
        parcel.writeString(goodComments)
        parcel.writeLongArray(points?.toLongArray())
        parcel.writeLongArray(providers?.toLongArray())
        parcel.writeValue(group)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Good> {
        override fun createFromParcel(parcel: Parcel): Good {
            return Good(parcel)
        }

        override fun newArray(size: Int): Array<Good?> {
            return arrayOfNulls(size)
        }
    }
}