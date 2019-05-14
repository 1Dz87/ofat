package ofat.my.ofat.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import ofat.my.ofat.model.GoodsGroup

@Dao
interface GoodsGroupDAO {

    @Insert
    fun save(goodsGroup: GoodsGroup)

    @Update
    fun update(goodsGroup: GoodsGroup)
}