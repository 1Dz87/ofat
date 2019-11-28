package ofat.my.ofat.persistence

import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ofat.my.ofat.model.Good
import androidx.room.Room
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.GoodsApi
import ofat.my.ofat.api.response.GetGoodResponse
import ofat.my.ofat.api.response.GoodsSyncResponse
import ofat.my.ofat.model.GoodsGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Database(entities = [Good::class, GoodsGroup::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OfatDatabase : RoomDatabase() {

    abstract fun goodDao(): GoodDAO

    abstract fun goodsGroupDao(): GoodsGroupDAO

    companion object {
        private var INSTANCE: OfatDatabase? = null

        fun getInstance(context: Context): OfatDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, OfatDatabase::class.java, "ofatData")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }

    fun destroyDataBase() {
        INSTANCE = null
    }

}