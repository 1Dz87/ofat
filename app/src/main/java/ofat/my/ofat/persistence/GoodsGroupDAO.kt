package ofat.my.ofat.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ofat.my.ofat.model.GoodsGroup

@Dao
interface GoodsGroupDAO : BaseDAO {

    @Insert
    fun save(goodsGroup: GoodsGroup)

    @Update
    fun update(goodsGroup: GoodsGroup)

    @Query("SELECT * FROM GoodsGroup WHERE id = :id")
    fun getById(id: Long): GoodsGroup?
}