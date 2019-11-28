package ofat.my.ofat.api

import ofat.my.ofat.api.response.*
import ofat.my.ofat.model.Good
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GoodsApi {

    @GET("synchronizeGoods")
    fun sync() : Call<GoodsSyncResponse>

    @POST("getGoods")
    fun getGoods(@Body fields: Map<String, @JvmSuppressWildcards Any?>): Call<GetGoodShortViewResponse>

    @GET("getGoodById")
    fun getGoodById(@Query("id")id: Long): Call<GetGoodResponse>

    @GET("getGoodShortViewByBarCode")
    fun getGoodShortView(@Query("barCode")barCode: String): Call<GetGoodShortViewResponse>

    @POST("createGood")
    fun createGood(@Body good: Good): Call<AddGoodResponse>

    @GET("deleteGood")
    fun deleteGood(@Query("id")id: Long): Call<DeleteGoodResponse>
}