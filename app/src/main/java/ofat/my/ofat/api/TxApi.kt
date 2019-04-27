package ofat.my.ofat.api

import ofat.my.ofat.api.response.TransactionResponse
import ofat.my.ofat.model.Transaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TxApi {

    @POST("doTransact")
    fun addClient(@Body tx: Transaction): Call<TransactionResponse>
}