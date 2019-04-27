package ofat.my.ofat.Util

import ofat.my.ofat.OfatApplication
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun toHumanDate(date : Date) : String {
        //return SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", OfatApplication.LOCALE).format(date)
        return SimpleDateFormat("dd.MM.yyyy", OfatApplication.LOCALE).format(date)
    }
}