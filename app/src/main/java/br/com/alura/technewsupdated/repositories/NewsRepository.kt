package br.com.alura.technewsupdated.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alura.technewsupdated.asynctask.BaseAsyncTask
import br.com.alura.technewsupdated.database.dao.NewsDAO
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.retrofit.webclient.NewsWebClient

class NewsRepository(
    private val dao: NewsDAO,
    private val webClient: NewsWebClient = NewsWebClient()
) {

    private val _news = MutableLiveData<Resource<List<News>>>()

    fun getAll(): LiveData<Resource<List<News>>> {
        val onSuccess: (List<News>) -> Unit = { _news.value = Resource(it) }
        getAllLocal(onSuccess = onSuccess)
        getAllRemote(onSuccess = onSuccess, onFailure = {
            _news.value = Resource(_news.value?.data, error = it)
        })
        return _news
    }

    fun getById(
        id: Long,
    ): LiveData<News?> {
        val liveData = MutableLiveData<News?>()
        BaseAsyncTask(onExecute = {
            dao.getById(id)!!
        }, onFinished = {
            liveData.value = it
        }).execute()
        return liveData
    }

    fun save(
        news: News,
    ): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        saveRemote(news, onSuccess = {
            liveData.value = Resource(null)
        }, onFailure = {
            liveData.value = Resource(null, error = it)
        })
        return liveData
    }

    fun remove(
        news: News,
    ) : LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        removeRemote(news, onSuccess = {
            liveData.value = Resource(null)
        }, onFailure = {
            liveData.value = Resource(null, error = it)
        })
        return liveData
    }

    fun edit(
        news: News,
    ): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        editRemote(news, onSuccess = {
            liveData.value = Resource(null)
        }, onFailure = {
            liveData.value = Resource(null, error = it)
        })
        return liveData
    }

    private fun editRemote(
        news: News,
        onSuccess: (editedNews: News) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webClient.edit(
            news.id, news, onSuccess = { editedNew ->
                editedNew?.let {
                    saveLocal(editedNew, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

    private fun removeRemote(
        news: News,
        onSuccess: () -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webClient.remove(news.id, onSuccess = {
            removeLocal(news, onSuccess)
        }, onFailure = onFailure)
    }

    private fun removeLocal(news: News, onSuccess: () -> Unit) {
        BaseAsyncTask(onExecute = {
            dao.remove(news)
        }, onFinished = {
            onSuccess()
        }).execute()
    }

    private fun saveRemote(
        news: News,
        onSuccess: (news: News) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webClient.save(
            news,
            onSuccess = {
                it?.let { savedNew ->
                    saveLocal(savedNew, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

    private fun getAllRemote(onSuccess: (List<News>) -> Unit, onFailure: (error: String?) -> Unit) {
        webClient.getAll(
            onSuccess = { currentNews ->
                currentNews?.let {
                    saveLocal(currentNews, onSuccess)
                    onSuccess(currentNews)
                }
            }, onFailure = onFailure
        )
    }

    private fun getAllLocal(onSuccess: (List<News>) -> Unit) {
        BaseAsyncTask(onExecute = {
            dao.getAll()
        }, onFinished = onSuccess).execute()
    }

    private fun saveLocal(
        news: List<News>,
        onSuccess: (currentNews: List<News>) -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(news)
            dao.getAll()
        }, onFinished = onSuccess).execute()
    }

    private fun saveLocal(
        news: News,
        onSuccess: (currentNews: News) -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(news)
            dao.getById(news.id)
        }, onFinished = { foundNew ->
            foundNew?.let {
                onSuccess(it)
            }
        }).execute()
    }
}