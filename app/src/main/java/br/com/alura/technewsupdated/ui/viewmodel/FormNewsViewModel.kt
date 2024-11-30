package br.com.alura.technewsupdated.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.repositories.Resource

class FormNewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    fun save(news: News): LiveData<Resource<Void?>> {
        if (news.id > 0) {
            return repository.edit(news)
        }
        return repository.save(news)
    }

    fun getById(id: Long): LiveData<News?> =
        repository.getById(id)
}