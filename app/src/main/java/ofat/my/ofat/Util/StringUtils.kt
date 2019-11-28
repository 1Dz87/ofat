package ofat.my.ofat.Util

object StringUtils {

    val EMPTY: String = ""

    fun isBlanc(str: String?) : Boolean {
        return str == null || str == ""
    }

    fun isNotBlanc(str: String?) : Boolean {
        return !isBlanc(str)
    }
}