package ofat.my.ofat.api

import ofat.my.ofat.api.response.AuthResponse
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    @FormUrlEncoded
    fun login(@FieldMap params: Map<String, String>): Call<AuthResponse>
}