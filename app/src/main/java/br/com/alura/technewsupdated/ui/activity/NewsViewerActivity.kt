package br.com.alura.technewsupdated.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technewsupdated.R
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.databinding.ActivityNewsViewerBinding
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.activity.extensions.showError
import br.com.alura.technewsupdated.ui.viewmodel.ListNewsViewModel
import br.com.alura.technewsupdated.ui.viewmodel.NewsViewerViewModel
import br.com.alura.technewsupdated.ui.viewmodel.factory.ListNewsViewModelFactory
import br.com.alura.technewsupdated.ui.viewmodel.factory.NewsViewerViewModelFactory

private const val NOT_FOUND_NEWS = "Notícia não encontrada"
private const val APPBAR_TITLE = "Notícia"
private const val FAILURE_ON_REMOVE_MSG = "Não foi possível remover notícia"

class NewsViewerActivity : AppCompatActivity() {

    private val newId: Long by lazy {
        intent.getLongExtra(NEWS_ID_KEY, 0)
    }

    private val viewModel by lazy {
        val factory =
            NewsViewerViewModelFactory(
                newId,
                NewsRepository(AppDatabase.getInstance(this).newsDAO),
            )
        ViewModelProvider(this, factory)[NewsViewerViewModel::class.java]
    }

    private lateinit var binding: ActivityNewsViewerBinding

    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = APPBAR_TITLE
        checkNewsId()
        findSelectedNew()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.news_viewer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_news_viewer_menu_edit -> openEditForm()
            R.id.item_news_viewer_menu_remove -> removeItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeItem() {
        if (::news.isInitialized) {
            viewModel.remove().observe(this, Observer { resource ->
                resource?.error?.let { showError(FAILURE_ON_REMOVE_MSG) }
                finish()
            })
        }
    }

    private fun openEditForm() {
        val intent = Intent(this, FormNewsActivity::class.java)
        intent.putExtra(NEWS_ID_KEY, newId)
        startActivity(intent)
    }

    private fun findSelectedNew() {
        viewModel.foundNews.observe(this, Observer { news ->
            news?.let {
                this.news = it
                fillFields(it)
            }
        })
    }

    private fun fillFields(news: News) {
        binding.tvNewsViewerActivityTitle.text = news.titulo
        binding.tvNewsViewerActivityText.text = news.texto
    }

    private fun checkNewsId() {
        if (newId == 0L) {
            showError(NOT_FOUND_NEWS)
            finish()
        }
    }
}