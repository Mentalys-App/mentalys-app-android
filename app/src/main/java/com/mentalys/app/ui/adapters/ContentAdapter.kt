package com.mentalys.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.ArticleContentEntity
import com.mentalys.app.databinding.ItemContentEmbedBinding
import com.mentalys.app.databinding.ItemContentHeaderBinding
import com.mentalys.app.databinding.ItemContentImageBinding
import com.mentalys.app.databinding.ItemContentListBinding
import com.mentalys.app.databinding.ItemContentParagraphBinding
import com.mentalys.app.databinding.ItemContentQuoteBinding
import com.mentalys.app.databinding.ItemContentSubheaderBinding
import java.util.regex.Pattern

class ContentAdapter : ListAdapter<ArticleContentEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            "header" -> R.layout.item_content_header
            "subheader" -> R.layout.item_content_subheader
            "paragraph" -> R.layout.item_content_paragraph
            "image" -> R.layout.item_content_image
            "quote" -> R.layout.item_content_quote
            "list" -> R.layout.item_content_list
            "embed" -> R.layout.item_content_embed
            else -> R.layout.item_content_paragraph
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_content_header -> {
                val binding = ItemContentHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            R.layout.item_content_subheader -> {
                val binding = ItemContentSubheaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SubHeaderViewHolder(binding)
            }

            R.layout.item_content_paragraph -> {
                val binding = ItemContentParagraphBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ParagraphViewHolder(binding)
            }

            R.layout.item_content_image -> {
                val binding = ItemContentImageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ImageViewHolder(binding)
            }

            R.layout.item_content_quote -> {
                val binding = ItemContentQuoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                QuoteViewHolder(binding)
            }

            R.layout.item_content_list -> {
                val binding = ItemContentListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ListViewHolder(binding)
            }

            R.layout.item_content_embed -> {
                val binding = ItemContentEmbedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EmbedViewHolder(binding)
            }

            else -> {
                val binding = ItemContentParagraphBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ParagraphViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val content = getItem(position)
        when (holder) {
            is HeaderViewHolder -> holder.bind(content)
            is SubHeaderViewHolder -> holder.bind(content)
            is ParagraphViewHolder -> holder.bind(content)
            is ImageViewHolder -> holder.bind(content)
            is QuoteViewHolder -> holder.bind(content)
            is ListViewHolder -> holder.bind(content)
            is EmbedViewHolder -> holder.bind(content)
        }
    }

//    override fun getItemCount() = contentList.size

    // View holders for each type of content
    class HeaderViewHolder(private var binding: ItemContentHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            binding.textView.text = contentEntity.text
        }
    }

    class SubHeaderViewHolder(private var binding: ItemContentSubheaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            binding.textView.text = contentEntity.text
        }
    }

    class ParagraphViewHolder(private var binding: ItemContentParagraphBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            binding.textView.text = contentEntity.text
        }
    }

    class ImageViewHolder(private var binding: ItemContentImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            Glide.with(itemView.context).load(contentEntity.src).into(binding.imageView)
            binding.textView.text = contentEntity.caption
        }
    }

    class QuoteViewHolder(private var binding: ItemContentQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            binding.quoteTextView.text = contentEntity.text
            binding.authorTextView.text = contentEntity.author
        }
    }

    class ListViewHolder(private var binding: ItemContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            val listText = contentEntity.items?.joinToString("\n") ?: ""
            binding.textView.text = listText
        }
    }

    class EmbedViewHolder(private var binding: ItemContentEmbedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contentEntity: ArticleContentEntity) {
            // YouTube URL from the content or a hardcoded link
            val youtubeUrl = contentEntity.url.toString()

            // Extract video ID and build the thumbnail URL
            val videoId = extractYoutubeId(youtubeUrl)
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"

            // Load thumbnail with Glide into the ImageView
            Glide.with(binding.root.context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(binding.imageView)
        }

        // Function to extract video ID from URL
        private fun extractYoutubeId(url: String): String? {
            val pattern =
                Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\/%2F|youtu.be%2F|\\/v%2F)[^#\\&\\?\\n]*")
            val matcher = pattern.matcher(url)
            return if (matcher.find()) matcher.group() else null
        }

    }


    companion object {

        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleContentEntity>() {
            override fun areItemsTheSame(
                oldItem: ArticleContentEntity,
                newItem: ArticleContentEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticleContentEntity,
                newItem: ArticleContentEntity
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

}
