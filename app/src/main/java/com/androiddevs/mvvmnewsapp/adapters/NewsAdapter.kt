package id.alpine.beritamvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.alpine.beritamvvm.R
import id.alpine.beritamvvm.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val artice = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(artice.urlToImage).into(ivArticleImage)
            tvSource.text = artice.source?.name
            tvTitle.text = artice.title
            tvDescription.text = artice.description
            tvPublishedAt.text = artice.publishedAt
            setOnClickListener {
                onItemCLickListener?.let { it(artice) }
            }
        }
    }

    private var onItemCLickListener: ((Article) -> Unit)? = null
    fun setOnCLickListener(listener: (Article) -> Unit) {
        onItemCLickListener = listener
    }
}