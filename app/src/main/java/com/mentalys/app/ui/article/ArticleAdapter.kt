package com.mentalys.app.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.databinding.ItemDataArticleBinding
import com.mentalys.app.databinding.ItemShimmerArticleBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class ArticleAdapter(
    private var isLoading: Boolean = true,
//    private val items: List<ArticleListItem>
) : ListAdapter<ArticleListEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemDataArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder && !isLoading) {
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

    class MyViewHolder(val binding: ItemDataArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleListEntity) {
            binding.apply {
                articleTitleTextView.text = article.title
                articleDescriptionTextView.text = article.metadata?.shortDescription
                articleAuthorTextView.text = "by ${article.author?.name}"
                articleReadTimeTextView.text = "• ${article.metadata?.readingTime ?: 0} min read"

                Glide.with(root.context)
                    .load(article.metadata?.imageLink)
                    .error(R.drawable.ic_image)
                    // .transform(RoundedCorners(16))
                    .into(articleImageView)

                // Format tags
                val formattedTags = article.metadata?.tags?.joinToString(" ") { "#$it" } ?: ""
                articleTagsTextView.text = formattedTags

                // Convert and format the date
                val formattedDate = article.metadata?.publishDate?.let { isoDate ->
                    convertDateToFormattedString(isoDate)
                } ?: "Unknown Date" // Fallback if date is null
                articleDateTextView.text = formattedDate
            }
        }

        private fun convertDateToFormattedString(isoDate: String): String {
            return try {
                // Parse ISO 8601 date
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                isoFormat.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC timezone
                val date = isoFormat.parse(isoDate) // Convert to Date object

                // Format to desired style
                val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                outputFormat.format(date)
            } catch (e: Exception) {
                "Invalid Date" // Fallback in case of an error
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) 3 else 3 // super.getItemCount()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    class ShimmerViewHolder(binding: ItemShimmerArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleListEntity>() {
            override fun areItemsTheSame(
                oldItem: ArticleListEntity,
                newItem: ArticleListEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ArticleListEntity,
                newItem: ArticleListEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
