package ofat.my.ofat.ui.main.goods

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.api.response.GetGoodResponse
import ofat.my.ofat.exception.ConnectionException
import ofat.my.ofat.exception.OfatCommonException
import ofat.my.ofat.exception.UnknownException
import ofat.my.ofat.model.Good
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodViewModel : ViewModel() {

    val currentGood = MutableLiveData<Good>()

    var cart = MutableLiveData<MutableMap<Good, Pair<Double, Double>>>()

    fun getBuyCart(): MutableLiveData<MutableMap<Good, Pair<Double, Double>>> {
        return if (CollectionUtils.mapIsEmpty(cart.value)) {
            cart.value = mutableMapOf()
            cart
        } else {
            cart
        }
    }

    fun clearCart() {
        cart.value?.clear()
    }

    fun getCurrentGood(id: Long?): MutableLiveData<Good> {
        if (id == null) throw OfatCommonException("Unexpected error")
        return if (currentGood.value == null || currentGood.value?.id != id) {
            loadGood(id)
            currentGood
        } else {
            currentGood
        }
    }

    private fun loadGood(id: Long) {
        val call = OfatApplication.goodApi?.getGoodById(id)
        call?.enqueue(object : Callback<GetGoodResponse> {
            override fun onFailure(call: Call<GetGoodResponse>, t: Throwable) {
                throw ConnectionException("Соединение разорвано или сервер не отвечает")
            }

            override fun onResponse(call: Call<GetGoodResponse>, response: Response<GetGoodResponse>) {
                if (response.body() != null && response.body()?.success != null) {
                    currentGood.value = response.body()?.success as Good
                } else {
                    throw UnknownException("Неизвестная ошибка")
                }
            }
        })
    }

    val barcode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
}