package br.com.alura.technewsupdated.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.alura.technewsupdated.R
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.databinding.ActivityNewsViewerBinding
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.activity.extensions.showError

private const val NOT_FOUND_NEWS = "Notícia não encontrada"
private const val APPBAR_TITLE = "Notícia"
private const val FAILURE_ON_REMOVE_MSG = "Não foi possível remover notícia"
class NewsViewerActivity : AppCompatActivity() {

    private val newId: Long by lazy {
        intent.getLongExtra(NEWS_ID_KEY, 0)
    }

    private val repository by lazy {
        NewsRepository(AppDatabase.getInstance(this).newsDAO)
    }

    private lateinit var binding: ActivityNewsViewerBinding

    private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = APPBAR_TITLE
        checkNewsId()
    }

    override fun onResume() {
        super.onResume()
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
            repository.remove(news, onSuccess = {
                finish()
            }, onFailure = {
                showError(FAILURE_ON_REMOVE_MSG)
            })
        }
    }

    private fun openEditForm() {
        val intent = Intent(this, FormNewsActivity::class.java)
        intent.putExtra(NEWS_ID_KEY, newId)
        startActivity(intent)
    }

    private fun findSelectedNew() {
        repository.getById(newId, onSuccess = {foundNew ->
            foundNew.let {
                this.news = it
                fillFields(it)
            }
        })
    }

    private fun fillFields(news: News) {
        binding.tvNewsViewerActivityTitle.text = news.title
        binding.tvNewsViewerActivityText.text = news.content
    }

    private fun checkNewsId() {
       if (newId == 0L) {
           showError(NOT_FOUND_NEWS)
           finish()
       }
    }
}