package br.com.alura.technewsupdated.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.viewmodel.ListNewsViewModel

@Suppress("UNCHECKED_CAST")
class ListNewsViewModelFactory(
    private val repository: NewsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListNewsViewModel(repository) as T
    }
}