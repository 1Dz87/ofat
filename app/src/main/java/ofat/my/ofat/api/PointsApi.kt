package ofat.my.ofat.api

import ofat.my.ofat.api.response.GetPointsResponse
import retrofit2.Call
import retrofit2.http.GET

interface PointsApi {

    @GET("getPoints")
    fun getPoints(): Call<GetPointsResponse>
}