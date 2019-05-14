package ofat.my.ofat.api

import ofat.my.ofat.api.response.CreateBookkeepersResponse
import ofat.my.ofat.api.response.GetBookkeepersResponse
import ofat.my.ofat.model.Bookkeeper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BookkeeperApi {

    @POST("createBookkeeper")
    fun createBookkeeper(@Body bookkeeper: Bookkeeper): Call<CreateBookkeepersResponse>

    @GET("getBookkeepers")
    fun getBookkeepers(): Call<GetBookkeepersResponse>
}