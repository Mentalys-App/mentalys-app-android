package com.mentalys.app.ui.music

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.data.local.entity.MusicItemEntity
import com.mentalys.app.databinding.ItemDataMusicBinding
import com.mentalys.app.databinding.ItemShimmerMusicBinding

class MusicAdapter(
    private var isLoading: Boolean = true,
    // private val items: List<ArticleListItem>
    private val fragmentManager: FragmentManager
) : ListAdapter<MusicItemEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerMusicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemDataMusicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder && !isLoading) {
            val event = getItem(position)
            holder.bind(event, fragmentManager)
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

    override fun getItemCount(): Int {
        return if (isLoading || currentList.isEmpty()) 5 else currentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    class ShimmerViewHolder(binding: ItemShimmerMusicBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MyViewHolder(val binding: ItemDataMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(music: MusicItemEntity, fragmentManager: FragmentManager) {
            binding.apply {
                binding.titleTextView.text = music.name
                binding.titleDescription.text = "by ${music.username}"
                itemView.setOnClickListener {
                    val bottomSheet = MusicBottomSheet(music.id)
                    bottomSheet.show(fragmentManager, "MusicBottomSheet")
                }
            }
        }
    }



    companion object {

        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MusicItemEntity>() {
            override fun areItemsTheSame(
                oldItem: MusicItemEntity,
                newItem: MusicItemEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: MusicItemEntity,
                newItem: MusicItemEntity
            ): Boolean {
                return oldItem == newItem
            }
        }

    }


}