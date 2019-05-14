package ofat.my.ofat.api

import ofat.my.ofat.api.response.GoodsGroupResponse
import ofat.my.ofat.model.GoodsGroup
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GoodsGroupApi {

    @POST("createGoodsGroup")
    fun createGoodsGroup(@Body group: GoodsGroup) : Call<GoodsGroupResponse>
}