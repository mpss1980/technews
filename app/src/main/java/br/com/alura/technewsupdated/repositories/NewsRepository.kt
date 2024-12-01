package br.com.alura.technewsupdated.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.alura.technewsupdated.asynctask.BaseAsyncTask
import br.com.alura.technewsupdated.database.dao.NewsDAO
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.retrofit.webclient.NewsWebClient

class NewsRepository(
    private val dao: NewsDAO,
    private val webClient: NewsWebClient
) {
    private val _newsMediator = MediatorLiveData<Resource<List<News>>>()

    fun getAll(): LiveData<Resource<List<News>>> {
        _newsMediator.addSource(getAllLocal(), Observer {
            _newsMediator.value = Resource(it)
        })

        val news = MutableLiveData<Resource<List<News>>>()
        _newsMediator.addSource(news) {failedResource ->
            val currentResource = _newsMediator.value
            val newResource = if (currentResource != null) {
                Resource(currentResource.data, failedResource?.error)
            } else {
                failedResource
            }
            _newsMediator.value = newResource
        }

        getAllRemote(onFailure = {
            news.value = Resource(news.value?.data, error = it)
        })
        return _newsMediator
    }

    fun getById(
        id: Long,
    ): LiveData<News?> {
        return dao.getById(id)
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
    ): LiveData<Resource<Void?>> {
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
        onSuccess: () -> Unit,
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
        onSuccess: () -> Unit,
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

    private fun getAllRemote(onFailure: (error: String?) -> Unit) {
        webClient.getAll(
            onSuccess = { currentNews ->
                currentNews?.let {
                    saveLocal(currentNews)
                }
            }, onFailure = onFailure
        )
    }

    private fun getAllLocal(): LiveData<List<News>> {
        return dao.getAll()
    }

    private fun saveLocal(
        news: List<News>,
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(news)
        }, onFinished = {}).execute()
    }

    private fun saveLocal(
        news: News,
        onSuccess: () -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(news)
        }, onFinished = {
            onSuccess()
        }).execute()
    }
}