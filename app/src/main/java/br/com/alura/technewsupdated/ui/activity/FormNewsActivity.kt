package br.com.alura.technewsupdated.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technewsupdated.R
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.databinding.ActivityFormNewsBinding
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.activity.extensions.showError
import br.com.alura.technewsupdated.ui.viewmodel.FormNewsViewModel
import br.com.alura.technewsupdated.ui.viewmodel.factory.FormNewsViewModelFactory

private const val APPBAR_EDIT_TITLE = "Editando notícia"
private const val APPBAR_CREATE_TITLE = "Criando notícia"
private const val ERROR_ON_SAVE = "Não foi possível salvar notícia"

class FormNewsActivity : AppCompatActivity() {

    private val newsId: Long by lazy {
        intent.getLongExtra(NEWS_ID_KEY, 0)
    }

    private val viewModel by lazy {
        val factory =
            FormNewsViewModelFactory(NewsRepository(AppDatabase.getInstance(this).newsDAO))
        ViewModelProvider(this, factory)[FormNewsViewModel::class.java]
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
        viewModel.save(news).observe(this, Observer { resource ->
            resource?.error?.let { showError(ERROR_ON_SAVE) }
            finish()
        })
    }

    private fun fillForm() {
        if (newsId <= 0) return
        viewModel.getById(newsId).observe(this, Observer { resource ->
            resource?.let { foundNew ->
                binding.etFormNewsActivityTitle.setText(foundNew.titulo)
                binding.etFormNewsActivityContent.setText(foundNew.texto)
            }
        })
    }

    private fun defineTitle() {
        title = if (newsId > 0)
            APPBAR_EDIT_TITLE else APPBAR_CREATE_TITLE
    }
}