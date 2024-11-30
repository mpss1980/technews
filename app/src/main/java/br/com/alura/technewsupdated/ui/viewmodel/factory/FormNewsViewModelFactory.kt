package br.com.alura.technewsupdated.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.viewmodel.FormNewsViewModel

@Suppress("UNCHECKED_CAST")
class FormNewsViewModelFactory(
    private val repository: NewsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FormNewsViewModel(repository) as T
    }
}