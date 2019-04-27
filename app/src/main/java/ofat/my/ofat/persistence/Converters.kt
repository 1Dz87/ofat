package ofat.my.ofat.persistence

import androidx.room.TypeConverter
import ofat.my.ofat.model.GoodStatus
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromInt(value: Int?): GoodStatus? {
        return value?.let { GoodStatus.values()[value] }
    }

    @TypeConverter
    fun toStatus(status: GoodStatus?): Int? {
        return status?.ordinal
    }

    @TypeConverter
    fun fromLong(longs: List<Long>): String {
        var result = StringBuilder()
        for (i in longs) {
            result.append(i.toString())
        }
        return result.toString()
    }

    @TypeConverter
    fun toLong(data: String): List<Long> {
        return data.split(",".toRegex()).dropLastWhile { it.isEmpty() }.map { it.toLong() }
    }

/*    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
            LocalDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return if (date == null) {
            null
        } else {
            date!!.toString()
        }
    }*/
}