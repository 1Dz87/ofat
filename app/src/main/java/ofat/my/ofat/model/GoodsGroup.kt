package ofat.my.ofat.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(indices = [Index("id")])
@Parcelize
data class GoodsGroup(
    @PrimaryKey var id: Long?,
    @ColumnInfo(name = "f_name")@SerializedName("name")@Expose var name: String?,
    var goods: List<Long>
    ) : Parcelable {
    constructor() : this (null, "", listOf())

    override fun toString(): String {
        return this.name.toString()
    }

    companion object GroupCREATOR : Parcelable.Creator<GoodsGroup> {
        override fun createFromParcel(parcel: Parcel): GoodsGroup {
            val id = parcel.readLong()
            val name = parcel.readString()
            var goods = listOf<Long>()
            parcel.readList(goods, Long::class.java.classLoader)
            return GoodsGroup(id, name, goods)
        }

        override fun newArray(size: Int): Array<GoodsGroup?> {
            return arrayOfNulls(size)
        }
    }
}