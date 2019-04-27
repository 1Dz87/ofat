package ofat.my.ofat.api

import ofat.my.ofat.api.response.CreateUserResponse
import ofat.my.ofat.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("addUser")
    fun addClient(@Body user: User): Call<CreateUserResponse>
}