package ofat.my.ofat.Util

import androidx.databinding.BindingConversion
import ofat.my.ofat.OfatApplication
import java.text.SimpleDateFormat
import java.util.*

public object DateUtil {

    fun toHumanDate(date : Date) : String {
        return SimpleDateFormat("dd.MM.yyyy", OfatApplication.LOCALE).format(date)
    }

    @JvmStatic
    @BindingConversion
    public fun toViewDateWithTime(date: Date?) : String {
        if (date != null) {
            return SimpleDateFormat("dd.MM.yyyy HH:mm", OfatApplication.LOCALE).format(date)
        } else {
            return StringUtils.EMPTY
        }
    }
}