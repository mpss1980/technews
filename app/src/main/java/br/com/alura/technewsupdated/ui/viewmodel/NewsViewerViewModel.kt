package br.com.alura.technewsupdated.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.repositories.Resource

class NewsViewerViewModel(
    id: Long,
    private val repository: NewsRepository
) : ViewModel() {

    val foundNews =  repository.getById(id)

    fun remove(): LiveData<Resource<Void?>> {
        return foundNews.value?.run { repository.remove(this) }
            ?: MutableLiveData<Resource<Void?>>().also {
                it.value = Resource(null, error = "Notícia não encontrada")
            }
    }
}