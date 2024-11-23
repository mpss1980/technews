package br.com.alura.technewsupdated.retrofit.webclient

import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.retrofit.AppRetrofit
import br.com.alura.technewsupdated.retrofit.service.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_FAILURE = "Request Failure"

class NewsWebClient(
    private val service: NewsService = AppRetrofit().newsService
) {

    fun getAll(
        onSuccess: (currentNews: List<News>?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        doRequest(
            service.getAll(),
            onSuccess,
            onFailure,
        )
    }

    fun save(
        news: News,
        onSuccess: (currentNews: News?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        doRequest(service.save(news), onSuccess, onFailure)
    }

    fun edit(
        id: Long,
        news: News,
        onSuccess: (currentNews: News?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        doRequest(service.edit(id, news), onSuccess, onFailure)
    }

    fun remove(
        id: Long,
        onSuccess: (currentNew: Void?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        doRequest(service.remove(id), onSuccess, onFailure)
    }

    private fun <T> doRequest(
        call: Call<T>,
        onSuccess: (currentNews: T?) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    return onSuccess(response.body())
                }
                onFailure(REQUEST_FAILURE)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t.message)
            }
        })
    }
}