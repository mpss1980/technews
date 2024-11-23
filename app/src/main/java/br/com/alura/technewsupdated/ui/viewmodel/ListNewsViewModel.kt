package br.com.alura.technewsupdated.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository

class ListNewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    fun getAll(): LiveData<List<News>> {
        return repository.getAll()
    }
}