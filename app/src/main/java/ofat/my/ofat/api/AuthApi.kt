package ofat.my.ofat.api

import ofat.my.ofat.api.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    fun login(@Header("Authorization") basicAuth: Any): Call<AuthResponse>
}