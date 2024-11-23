package br.com.alura.technewsupdated.repositories

import br.com.alura.technewsupdated.asynctask.BaseAsyncTask
import br.com.alura.technewsupdated.database.dao.NewsDAO
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.retrofit.webclient.NewsWebClient

class NewsRepository(
    private val dao: NewsDAO,
    private val webClient: NewsWebClient = NewsWebClient()
) {

    fun getAll(
        onSuccess: (List<News>) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        getAllLocal(onSuccess)
        getAllRemote(onSuccess, onFailure)
    }

    fun getById(
        id: Long,
        onSuccess: (foundId: News) -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.getById(id)!!
        }, onFinished = onSuccess).execute()
    }

    fun save(
        news: News,
        onSuccess: (news: News) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        saveRemote(news, onSuccess, onFailure)
    }

    fun remove(
        news: News,
        onSuccess: () -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        removeRemote(news, onSuccess, onFailure)
    }

    fun edit(
        news: News,
        onSuccess: (editedNews: News) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        editRemote(news, onSuccess, onFailure)
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

    private fun removeRemote(news: News, onSuccess: () -> Unit, onFailure: (error: String?) -> Unit) {
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