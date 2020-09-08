package ofat.my.ofat.Util

import retrofit2.Response

object WebUtil {

    fun <T> checkUnauthCode(response: Response<T>, defaultString: String, defaultFunc: (() -> Unit)?) : String? {
        if (response.code() == 401) {
            return response.errorBody()?.string()
        }
        if (defaultFunc != null) {
            defaultFunc()
        }
        return defaultString
    }
}