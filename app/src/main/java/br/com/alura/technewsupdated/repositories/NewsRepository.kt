package br.com.alura.technewsupdated.repositories

import br.com.alura.technewsupdated.asynctask.BaseAsyncTask
import br.com.alura.technewsupdated.database.dao.NewsDAO
import br.com.alura.technewsupdated.model.New
import br.com.alura.technewsupdated.retrofit.webclient.NewsWebClient

class NewsRepository(
    private val dao: NewsDAO,
    private val webClient: NewsWebClient = NewsWebClient()
) {

    fun getAll(
        onSuccess: (List<New>) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        getAllLocal(onSuccess)
        getAllRemote(onSuccess, onFailure)
    }

    fun getById(
        id: Long,
        onSuccess: (foundId: New) -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.getById(id)!!
        }, onFinished = onSuccess).execute()
    }

    fun save(
        new: New,
        onSuccess: (new: New) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        saveRemote(new, onSuccess, onFailure)
    }

    fun remove(
        new: New,
        onSuccess: () -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        removeRemote(new, onSuccess, onFailure)
    }

    fun edit(
        new: New,
        onSuccess: (editedNew: New) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        editRemote(new, onSuccess, onFailure)
    }

    private fun editRemote(
        new: New,
        onSuccess: (editedNew: New) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webClient.edit(
            new.id, new, onSuccess = { editedNew ->
                editedNew?.let {
                    saveLocal(editedNew, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

    private fun removeRemote(new: New, onSuccess: () -> Unit, onFailure: (error: String?) -> Unit) {
        webClient.remove(new.id, onSuccess = {
            removeLocal(new, onSuccess)
        }, onFailure = onFailure)
    }

    private fun removeLocal(new: New, onSuccess: () -> Unit) {
        BaseAsyncTask(onExecute = {
            dao.remove(new)
        }, onFinished = {
            onSuccess()
        }).execute()
    }

    private fun saveRemote(
        new: New,
        onSuccess: (new: New) -> Unit,
        onFailure: (error: String?) -> Unit
    ) {
        webClient.save(
            new,
            onSuccess = {
                it?.let { savedNew ->
                    saveLocal(savedNew, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

    private fun getAllRemote(onSuccess: (List<New>) -> Unit, onFailure: (error: String?) -> Unit) {
        webClient.getAll(
            onSuccess = { currentNews ->
                currentNews?.let {
                    saveLocal(currentNews, onSuccess)
                }
            }, onFailure = onFailure
        )
    }

    private fun getAllLocal(onSuccess: (List<New>) -> Unit) {
        BaseAsyncTask(onExecute = {
            dao.getAll()
        }, onFinished = onSuccess).execute()
    }

    private fun saveLocal(
        news: List<New>,
        onSuccess: (currentNews: List<New>) -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(news)
            dao.getAll()
        }, onFinished = onSuccess).execute()
    }

    private fun saveLocal(
        new: New,
        onSuccess: (currentNew: New) -> Unit
    ) {
        BaseAsyncTask(onExecute = {
            dao.save(new)
            dao.getById(new.id)
        }, onFinished = { foundNew ->
            foundNew?.let {
                onSuccess(it)
            }
        }).execute()
    }
}