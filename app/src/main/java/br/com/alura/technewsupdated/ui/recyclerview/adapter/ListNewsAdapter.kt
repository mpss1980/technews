package br.com.alura.technewsupdated.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.technewsupdated.databinding.NewsItemBinding
import br.com.alura.technewsupdated.model.News

class ListNewsAdapter(
    private val news: MutableList<News> = mutableListOf(),
    var onTap: (news: News) -> Unit = {}
) : RecyclerView.Adapter<ListNewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(news[position]) {
                binding.itemNewsTitle.text = title
                binding.itemNewsContent.text = content

                holder.itemView.setOnClickListener {
                    onTap(this)
                }
            }
        }
    }

    fun updateNews(news: List<News>) {
        notifyItemRangeRemoved(0, this.news.size)
        this.news.clear()
        this.news.addAll(news)
        notifyItemRangeInserted(0, this.news.size)
    }

    inner class ViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)
}