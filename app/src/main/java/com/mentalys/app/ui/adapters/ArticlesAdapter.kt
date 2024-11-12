package com.mentalys.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mentalys.app.R


class ArticleAdapter(private val items: List<ArticleItem>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.article_image_view)
        val titleTextView: TextView = itemView.findViewById(R.id.article_title_text_view)
        val descriptionTextView: TextView = itemView.findViewById(R.id.article_description_text_view)
        val articleAuthorTextView: TextView = itemView.findViewById(R.id.article_author_text_view)
        val articleDateTextView: TextView = itemView.findViewById(R.id.article_date_text_view)
        val articleReadTimeTextView: TextView = itemView.findViewById(R.id.article_read_time_text_view)
        val articleTagsTextView: TextView = itemView.findViewById(R.id.article_tags_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imageView.context)
            .load(item.imageResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        holder.articleAuthorTextView.text = item.author
        holder.articleDateTextView.text = item.date
        holder.articleReadTimeTextView.text = item.readTime
        holder.articleTagsTextView.text = item.tags
    }

    override fun getItemCount() = items.size
}

data class ArticleItem(
    val imageResId: Int,
    val title: String,
    val description: String,
    val author: String,
    val date: String,
    val readTime: String,
    val tags: String
)