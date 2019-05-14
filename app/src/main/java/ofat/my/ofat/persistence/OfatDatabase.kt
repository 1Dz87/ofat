package ofat.my.ofat.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ofat.my.ofat.model.Good
import androidx.room.Room
import ofat.my.ofat.model.GoodsGroup

@Database(entities = [Good::class, GoodsGroup::class], version = 3, exportSchema = false)
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

    fun destroyDataBase(){
        INSTANCE = null
    }

}