package ofat.my.ofat.api

import ofat.my.ofat.api.response.GetPointsResponse
import retrofit2.Call
import retrofit2.http.POST

interface PointsApi {

    @POST("getPoints")
    fun getPoints(): Call<GetPointsResponse>
}