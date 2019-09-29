package ofat.my.ofat.api

import ofat.my.ofat.api.response.QueryBookResponseObj
import ofat.my.ofat.model.QueryBookRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TemplatesApi {

    @POST("queryTemplate")
    fun queryTemplate(@Body queryRequest: QueryBookRequest): Call<QueryBookResponseObj>
}