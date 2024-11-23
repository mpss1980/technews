package br.com.alura.technewsupdated.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.alura.technewsupdated.R
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.databinding.ActivityFormNewsBinding
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.activity.extensions.showError

private const val APPBAR_EDIT_TITLE = "Editando notícia"
private const val APPBAR_CREATE_TITLE = "Criando notícia"
private const val ERROR_ON_SAVE = "Não foi possível salvar notícia"

class FormNewsActivity : AppCompatActivity() {

    private val newsId: Long by lazy {
        intent.getLongExtra(NEWS_ID_KEY, 0)
    }

    private val repository by lazy {
        NewsRepository(AppDatabase.getInstance(this).newsDAO)
    }

    private lateinit var binding: ActivityFormNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defineTitle()
        fillForm()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_form_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save_form_news -> {
                val title = binding.etFormNewsActivityTitle.text.toString()
                val content = binding.etFormNewsActivityContent.text.toString()
                save(News(newsId, title, content))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save(news: News) {
        val failure = { _: String? -> showError(ERROR_ON_SAVE) }
        val success = { _: News -> finish() }

        if (news.id > 0) {
            return repository.edit(news, onSuccess = success, onFailure = failure)
        }
        repository.save(news, onSuccess = success, onFailure = failure)
    }

    private fun fillForm() {
        repository.getById(newsId, onSuccess = { foundNew ->
            binding.etFormNewsActivityTitle.setText(foundNew.title)
            binding.etFormNewsActivityContent.setText(foundNew.content)
        })
    }

    private fun defineTitle() {
        title = if (newsId > 0)
            APPBAR_EDIT_TITLE else APPBAR_CREATE_TITLE
    }
}