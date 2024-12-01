package br.com.alura.technewsupdated.ui.modules

import androidx.room.Room
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.database.dao.NewsDAO
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.retrofit.webclient.NewsWebClient
import br.com.alura.technewsupdated.ui.viewmodel.FormNewsViewModel
import br.com.alura.technewsupdated.ui.viewmodel.ListNewsViewModel
import br.com.alura.technewsupdated.ui.viewmodel.NewsViewerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE_NAME = "news.db"

val appModules = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single<NewsDAO> { get<AppDatabase>().newsDAO }
    single<NewsWebClient> { NewsWebClient() }
    single<NewsRepository> { NewsRepository(get(), get()) }
    viewModel<ListNewsViewModel> { ListNewsViewModel(get()) }
    viewModel<NewsViewerViewModel> { (newsId: Long) -> NewsViewerViewModel(newsId, get()) }
    viewModel<FormNewsViewModel> { FormNewsViewModel(get()) }
}