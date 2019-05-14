package ofat.my.ofat.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class GoodsGroup(
    @PrimaryKey var id: Long?,
    @ColumnInfo(name = "f_name")@SerializedName("name")@Expose var name: String,
    var goods: List<Long>
    ) : Parcelable {
    constructor() : this (null, "", listOf())
}