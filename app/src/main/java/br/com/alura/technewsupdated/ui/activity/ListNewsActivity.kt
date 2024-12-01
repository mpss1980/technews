package br.com.alura.technewsupdated.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout.VERTICAL
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.alura.technewsupdated.databinding.ActivityListNewsBinding
import br.com.alura.technewsupdated.model.News
import br.com.alura.technewsupdated.ui.activity.extensions.showError
import br.com.alura.technewsupdated.ui.recyclerview.adapter.ListNewsAdapter
import br.com.alura.technewsupdated.ui.viewmodel.ListNewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val APPBAR_TITLE = "Notícias"
private const val LOAD_NEWS_FAILURE_MESSAGE = "Cannot load more news"

class ListNewsActivity : AppCompatActivity() {
    private val viewModel:ListNewsViewModel by viewModel()

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
        viewModel.getAll().observe(this, Observer { resource ->
            resource.data?.let { adapter.updateNews(it) }
            resource.error?.let { showError(LOAD_NEWS_FAILURE_MESSAGE) }
        })
    }
}