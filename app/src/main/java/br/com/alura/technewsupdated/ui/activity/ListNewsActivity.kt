package br.com.alura.technewsupdated.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.alura.technewsupdated.R
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.model.New
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.activity.extensions.showError
import br.com.alura.technewsupdated.ui.recyclerview.adapter.ListNewsAdapter
import kotlinx.android.synthetic.main.activity_list_news.fab_save_news
import kotlinx.android.synthetic.main.activity_list_news.rv_list_news

private const val LOAD_NEWS_FAILURE_MESSAGE = "Cannot load more news"

class ListNewsActivity : AppCompatActivity() {
    private val repository by lazy {
        NewsRepository(AppDatabase.getInstance(this).newsDAO)
    }

    private val adapter by lazy {
        ListNewsAdapter(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)
        configureRecyclerView()
        configureFabAddNew()
    }

    override fun onResume() {
        super.onResume()
        fetchNews()
    }

    private fun configureRecyclerView() {
        val divider = DividerItemDecoration(this, VERTICAL)
        rv_list_news.addItemDecoration(divider)
        rv_list_news.adapter = adapter
        configureAdapter()
    }

    private fun configureFabAddNew() {
        fab_save_news.setOnClickListener {
            openCreationModeForm()
        }
    }

    private fun configureAdapter() {
        adapter.onTap = this::openNewsViewer
    }

    private fun openNewsViewer(it: New) {
        val intent = Intent(this, NewsViewerActivity::class.java)
        intent.putExtra(NEWS_ID_KEY, it.id)
        startActivity(intent)
    }

    private fun openCreationModeForm() {
        val intent = Intent(this, FormNewsActivity::class.java)
        startActivity(intent)
    }

    private fun fetchNews() {
        repository.getAll(
            onSuccess = {
                adapter.updateNews(it)
            }, onFailure = {
                showError(LOAD_NEWS_FAILURE_MESSAGE)
            }
        )
    }
}