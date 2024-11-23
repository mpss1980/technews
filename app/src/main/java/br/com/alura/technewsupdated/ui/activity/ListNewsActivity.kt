package br.com.alura.technewsupdated.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.alura.technewsupdated.database.AppDatabase
import br.com.alura.technewsupdated.databinding.ActivityListNewsBinding
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.repositories.NewsRepository
import br.com.alura.technewsupdated.ui.activity.extensions.showError
import br.com.alura.technewsupdated.ui.recyclerview.adapter.ListNewsAdapter

private const val APPBAR_TITLE = "Notícias"
private const val LOAD_NEWS_FAILURE_MESSAGE = "Cannot load more news"

class ListNewsActivity : AppCompatActivity() {
    private val repository by lazy {
        NewsRepository(AppDatabase.getInstance(this).newsDAO)
    }

    private val adapter by lazy {
        ListNewsAdapter()
    }

    private lateinit var binding: ActivityListNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = APPBAR_TITLE
        configureRecyclerView()
        configureFabAddNew()
    }

    override fun onResume() {
        super.onResume()
        fetchNews()
    }

    private fun configureRecyclerView() {
        val divider = DividerItemDecoration(this, VERTICAL)
        binding.rvListNews.addItemDecoration(divider)
        binding.rvListNews.adapter = adapter
        configureAdapter()
    }

    private fun configureFabAddNew() {
        binding.fabSaveNews.setOnClickListener {
            openCreationModeForm()
        }
    }

    private fun configureAdapter() {
        adapter.onTap = this::openNewsViewer
    }

    private fun openNewsViewer(it: News) {
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