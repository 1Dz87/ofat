package ofat.my.ofat.api

import ofat.my.ofat.api.response.TransactionResponse
import ofat.my.ofat.model.Transaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TxApi {

    @POST("doTransact")
    fun doTransact(@Body tx: Set<Transaction>): Call<TransactionResponse>

    @POST("multipleTransaction")
    fun multipleTransaction(@Body txs: Set<Transaction>) : Call<TransactionResponse>
}