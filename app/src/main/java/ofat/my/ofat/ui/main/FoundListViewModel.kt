package ofat.my.ofat.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.api.response.GetGoodShortViewResponse
import ofat.my.ofat.exception.ConnectionException
import ofat.my.ofat.exception.OfatCommonException
import ofat.my.ofat.exception.UnknownException
import ofat.my.ofat.model.ShortView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoundListViewModel : ViewModel() {

    var foundList: MutableLiveData<MutableCollection<ShortView>?> = MutableLiveData(mutableListOf())

    fun getFoundList(barcode: String?) : MutableLiveData<MutableCollection<ShortView>?> {
        if (barcode == null) throw OfatCommonException("Unexpected error")
        return if (CollectionUtils.isEmpty(foundList.value) || foundList.value?.iterator()?.next()?.barcode != barcode) {
            barcodeRequest(barcode)
            foundList
        } else {
            foundList
        }
    }

    private fun barcodeRequest(rawValue: String) {
        val call = OfatApplication.goodApi?.getGoodShortView(rawValue)
        call?.enqueue(object : Callback<GetGoodShortViewResponse> {
            override fun onFailure(call: Call<GetGoodShortViewResponse>, t: Throwable) {
                throw ConnectionException("Соединение разорвано или сервер не отвечает")
            }

            override fun onResponse(
                call: Call<GetGoodShortViewResponse>,
                response: Response<GetGoodShortViewResponse>
            ) {
                if (response.body()?.success != null) {
                    foundList.value = response.body()?.success as MutableCollection<ShortView>
                } else {
                    throw UnknownException("Товар не найден.")
                }
            }
        })
    }
}
