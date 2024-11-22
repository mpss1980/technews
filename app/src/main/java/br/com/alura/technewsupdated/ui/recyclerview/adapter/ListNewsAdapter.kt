package br.com.alura.technewsupdated.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.technewsupdated.R
import br.com.alura.technewsupdated.model.New
import kotlinx.android.synthetic.main.news_item.view.item_news_content
import kotlinx.android.synthetic.main.news_item.view.item_news_title

class ListNewsAdapter(
    private val context: Context,
    private val news: MutableList<New> = mutableListOf(),
    var onTap: (new: New) -> Unit = {}
) : RecyclerView.Adapter<ListNewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val createdView = LayoutInflater.from(context)
            .inflate(R.layout.news_item, parent, false)
        return ViewHolder(createdView)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val new = news[position]
        holder.populateItemView(new)
    }

    fun updateNews(news: List<New>) {
        notifyItemRangeRemoved(0, this.news.size)
        this.news.clear()
        this.news.addAll(news)
        notifyItemRangeInserted(0, this.news.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var new: New

        init {
            itemView.setOnClickListener {
                if (::new.isInitialized) {
                    onTap(new)
                }
            }
        }

        fun populateItemView(new: New) {
            this.new = new
            itemView.item_news_title.text = new.title
            itemView.item_news_content.text = new.content
        }
    }
}