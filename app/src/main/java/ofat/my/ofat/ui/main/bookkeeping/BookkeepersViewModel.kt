package ofat.my.ofat.ui.main.bookkeeping

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.api.response.GetGoodResponse
import ofat.my.ofat.api.response.SingleTxResponse
import ofat.my.ofat.exception.ConnectionException
import ofat.my.ofat.exception.OfatCommonException
import ofat.my.ofat.exception.UnknownException
import ofat.my.ofat.model.Good
import ofat.my.ofat.model.QueryBookResponse
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookkeepersViewModel : ViewModel() {

    var queryGood: MutableLiveData<ShortView> = MutableLiveData()

    var queryUser: MutableLiveData<ShortView> = MutableLiveData()

    var queryResponse: MutableLiveData<QueryBookResponse> = MutableLiveData()

    var selectedTx = MutableLiveData<Transaction>()

    fun getSelectedTx(id: Long?, defaultFunction: (Transaction?) -> Unit) {
        if (id == null) throw OfatCommonException("Unexpected error")
        return if (selectedTx.value == null || selectedTx.value?.id != id) {
            loadTx(id, defaultFunction)
        } else {
            defaultFunction(selectedTx.value)
        }
    }

    private fun loadTx(id: Long, defaultFunction: (Transaction?) -> Unit) {
        val call = OfatApplication.txApi?.getTxById(id)
        call?.enqueue(object : Callback<SingleTxResponse> {
            override fun onFailure(call: Call<SingleTxResponse>, t: Throwable) {
                throw ConnectionException("Соединение разорвано или сервер не отвечает")
            }

            override fun onResponse(call: Call<SingleTxResponse>, response: Response<SingleTxResponse>) {
                if (response.body() != null && response.body()?.success != null) {
                    selectedTx.value = response.body()?.success as Transaction
                    defaultFunction(selectedTx.value)
                } else {
                    throw UnknownException("Неизвестная ошибка")
                }
            }
        })
    }
}