package ofat.my.ofat.api

import ofat.my.ofat.api.response.DeleteGoodResponse
import ofat.my.ofat.api.response.GoodsGroupNamesResponse
import ofat.my.ofat.api.response.GoodsGroupResponse
import ofat.my.ofat.api.response.GoodsGroupSVResponse
import ofat.my.ofat.model.GoodsGroup
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GoodsGroupApi {

    @POST("createGoodsGroup")
    fun createGoodsGroup(@Body group: GoodsGroup) : Call<GoodsGroupResponse>

    @GET("getGoodsGroupsNames")
    fun getGoodsGroupsNames() : Call<GoodsGroupNamesResponse>

    @GET("getGoodsGroupsSV")
    fun getGoodsGroupsSV() : Call<GoodsGroupSVResponse>

    @GET("getById")
    fun getById(@Query("id") id : Long) : Call<GoodsGroupResponse>

    @GET("deleteGroup")
    fun deleteGroup(@Query("id") id : Long) : Call<DeleteGoodResponse>

    @POST("updateGoodsGroup")
    fun updateGoodsGroup(@Body group: GoodsGroup) : Call<GoodsGroupResponse>
}