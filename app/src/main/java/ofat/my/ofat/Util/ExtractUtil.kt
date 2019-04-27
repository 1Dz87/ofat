package ofat.my.ofat.Util

import android.widget.TextView
import androidx.annotation.Nullable

object ExtractUtil {

    @Nullable
    fun <T : TextView> v(@Nullable view: T): String? {
        return value(view)
    }

    @Nullable
    fun <T : TextView> value(@Nullable view: T?): String? {
        if (view == null) {
            return null
        }
        val value = view.text.toString()
        return if (value.trim { it <= ' ' }.isEmpty()) null else value
    }
}