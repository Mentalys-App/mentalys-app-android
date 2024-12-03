package com.mentalys.app.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.databinding.ItemDataFoodBinding
import com.mentalys.app.databinding.ItemShimmerFoodBinding
import com.mentalys.app.utils.showToast

class FoodAdapter(
    private var isLoading: Boolean = true,
//    private val items: List<ArticleListItem>
) : ListAdapter<FoodEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {


    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerFoodBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemDataFoodBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder && !isLoading && currentList.isNotEmpty()) {
            if (currentList.isNotEmpty()) {
                val food = getItem(position)
                food?.let { holder.bind(it) }
            } else {

            }
//            val favoriteImageView = holder.binding.favoriteImageView
//            favoriteImageView.setImageResource(
//                if (event.isFavorite == true) R.drawable.ic_favorite
//                else R.drawable.ic_favorite_border
//            )
//            favoriteImageView.setOnClickListener {
//                onFavoriteClick(event)
//            }
        } else if (holder is ShimmerViewHolder) {

        }
    }

    class ShimmerViewHolder(binding: ItemShimmerFoodBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MyViewHolder(val binding: ItemDataFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodEntity) {
            binding.apply {
                foodTitleTextView.text = food.name
                foodDescriptionTextView.text = food.description

                Glide.with(root.context)
                    .load(food.imageUrl)
                    .error(R.drawable.ic_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(foodImageView)

                itemView.setOnClickListener {
                    showToast(root.context, "Clicked! ID: ${food.id}")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading || currentList.isNotEmpty()) 4 else super.getItemCount()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodEntity>() {
            override fun areItemsTheSame(
                oldItem: FoodEntity,
                newItem: FoodEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: FoodEntity,
                newItem: FoodEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
