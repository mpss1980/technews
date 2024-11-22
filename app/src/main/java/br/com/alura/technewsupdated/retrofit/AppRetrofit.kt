package br.com.alura.technewsupdated.retrofit

import br.com.alura.technewsupdated.retrofit.service.NewsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://seu_ip:porta_da_api/"

class AppRetrofit {
    private val client by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val newsService: NewsService by lazy {
        retrofit.create(NewsService::class.java)
    }
}