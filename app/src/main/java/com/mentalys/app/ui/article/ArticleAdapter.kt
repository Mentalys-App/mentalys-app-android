package com.mentalys.app.ui.article

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mentalys.app.R
import com.mentalys.app.data.remote.response.article.ArticleListItem
import com.mentalys.app.databinding.ItemArticleBinding


class ArticleAdapter(
//    private val items: List<ArticleListItem>
) :ListAdapter<ArticleListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == VIEW_TYPE_SHIMMER) {
//            val binding = ItemShimmerVerticalBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//            ShimmerViewHolder(binding)
//        } else {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) { // && !isLoading) {
            val event = getItem(position)
            holder.bind(event)
//            val favoriteImageView = holder.binding.favoriteImageView
//            favoriteImageView.setImageResource(
//                if (event.isFavorite == true) R.drawable.ic_favorite
//                else R.drawable.ic_favorite_border
//            )
//            favoriteImageView.setOnClickListener {
//                onFavoriteClick(event)
//            }
        }
    }

    class MyViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleListItem) {
//            Glide.with(binding.root.context)
//                .load(article.imageUrl)
//                .transform(RoundedCorners(16))
//                .into(binding.articleImageView)
            binding.articleTitleTextView.text = article.title
//            binding.articleDescriptionTextView.text = article.metadata.description
//            binding.articleAuthorTextView.text = article.metadata.author
//            binding.articleDateTextView.text = article.metadata.author
            binding.articleReadTimeTextView.text = article.metadata?.reading_time.toString()
//            binding.articleTagsTextView.text = article.metadata.tags
        }
    }

//    override fun getItemCount(): Int {
//        return if (isLoading) 5 else super.getItemCount()
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun setLoadingState(isLoading: Boolean) {
//        this.isLoading = isLoading
//        notifyDataSetChanged()
//    }

//    class ShimmerViewHolder(binding: ItemShimmerVerticalBinding) :
//        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleListItem>() {
            override fun areItemsTheSame(
                oldItem: ArticleListItem,
                newItem: ArticleListItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ArticleListItem,
                newItem: ArticleListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}

//data class ArticleItem(
//    val imageResId: Int,
//    val title: String,
//    val description: String,
//    val author: String,
//    val date: String,
//    val readTime: String,
//    val tags: String
//)