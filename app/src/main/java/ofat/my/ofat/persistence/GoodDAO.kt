package ofat.my.ofat.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ofat.my.ofat.model.Good

@Dao
interface GoodDAO : BaseDAO {

    @Query("SELECT * FROM good WHERE f_barcode = :barcode")
    fun getByBarcode(barcode: String): Good?

    @Query("SELECT * FROM good WHERE id = :id")
    fun getById(id: Long): Good?

    @Insert
    fun save(good: Good)

    @Update
    fun update(good: Good)
}