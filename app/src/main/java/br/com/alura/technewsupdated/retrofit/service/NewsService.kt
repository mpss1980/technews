package br.com.alura.technewsupdated.retrofit.service

import br.com.alura.technewsupdated.model.News
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NewsService {
    @GET("noticias")
    fun getAll(): Call<List<News>>

    @POST("noticias")
    fun save(@Body news: News): Call<News>

    @PUT("noticias/{id}")
    fun edit(@Path("id") id: Long, @Body news: News) : Call<News>

    @DELETE("noticias/{id}")
    fun remove(@Path("id") id: Long): Call<Void>
}