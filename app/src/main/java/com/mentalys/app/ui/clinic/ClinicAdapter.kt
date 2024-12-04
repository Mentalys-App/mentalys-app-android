package com.mentalys.app.ui.clinic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.ClinicEntity
import com.mentalys.app.databinding.ItemDataArticleBinding
import com.mentalys.app.databinding.ItemDataClinicBinding
import com.mentalys.app.databinding.ItemShimmerArticleBinding
import com.mentalys.app.ui.article.ArticleAdapter.MyViewHolder


class ClinicAdapter(
    private var isLoading: Boolean = true,
) : ListAdapter<ClinicEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

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
        }
    }

    class MyViewHolder(val binding: ItemDataClinicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clinic: ClinicEntity) {
            binding.apply {
                tvClinicName.text = clinic.name
                if (clinic.openNow == true) {
                    tvClinicTime.text = "Open"
                } else {
                    tvClinicTime.text = "Close"
                }
                tvClinicAddress.text = clinic.vicinity
                Glide.with(root.context)
                    .load(clinic.photoUrl)
                    .error(R.drawable.ic_image)
                    // .transform(RoundedCorners(16))
                    .into(imgClinic)

                // Handle Item Click
//                itemView.setOnClickListener {
//                    val intent = Intent(root.context, ArticleDetailActivity::class.java)
//                    intent.putExtra("EXTRA_ARTICLE_ID", article.id)
//                    root.context.startActivity(intent)
//                }
            }
        }
    }


    override fun getItemCount(): Int {
        return if (isLoading || currentList.isNotEmpty()) 3 else minOf(currentList.size, 3)
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

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClinicEntity>() {
            override fun areItemsTheSame(
                oldItem: ClinicEntity,
                newItem: ClinicEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ClinicEntity,
                newItem: ClinicEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

