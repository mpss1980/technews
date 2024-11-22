package br.com.alura.technewsupdated.retrofit.service

import br.com.alura.technewsupdated.model.New
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NewsService {
    @GET("noticias")
    fun getAll(): Call<List<New>>

    @POST("noticias")
    fun save(@Body new: New): Call<New>

    @PUT("noticias/{id}")
    fun edit(@Path("id") id: Long, @Body new: New) : Call<New>

    @DELETE("noticias/{id}")
    fun remove(@Path("id") id: Long): Call<Void>
}