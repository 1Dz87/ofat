package ofat.my.ofat.api

import ofat.my.ofat.api.response.SingleTxResponse
import ofat.my.ofat.api.response.TransactionResponse
import ofat.my.ofat.model.Transaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TxApi {

    @POST("doTransact")
    fun doTransact(@Body tx: Set<Transaction>): Call<TransactionResponse>

    @POST("multipleTransaction")
    fun multipleTransaction(@Body txs: Set<Transaction>) : Call<TransactionResponse>

    @GET("getTxById")
    fun getTxById(@Query("id") id: Long) : Call<SingleTxResponse>
}