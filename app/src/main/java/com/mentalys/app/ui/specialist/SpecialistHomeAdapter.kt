package com.mentalys.app.ui.specialist

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.databinding.ItemDataSpecialistHomeBinding
import com.mentalys.app.databinding.ItemShimmerSpecialistHomeBinding

class SpecialistHomeAdapter(
    private var isLoading: Boolean = true,
    // private val items: List<PsychiatristsItem>
) : ListAdapter<SpecialistEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerSpecialistHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemDataSpecialistHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            MyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder && !isLoading) {
            val data = getItem(position)
            holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading || currentList.isEmpty()) 3 else currentList.size// minOf(currentList.size, 3)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    class ShimmerViewHolder(binding: ItemShimmerSpecialistHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MyViewHolder(val binding: ItemDataSpecialistHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SpecialistEntity) {
            binding.apply {
                nameTextView.text = data.fullName
                roleTextView.text = data.mainRole
                experienceTextView.text = "${data.experienceYears} years experience"
                Glide.with(itemView)
                    .load(data.photoUrl)
                    .error(R.drawable.image_specialist_placeholder)
                    .placeholder(R.drawable.image_specialist_placeholder)
                    .into(binding.imageView)
                itemView.setOnClickListener {
                    val intent = Intent(root.context, SpecialistDetailActivity::class.java)
                    intent.putExtra(SpecialistDetailActivity.EXTRA_SPECIALIST_ID, data.id)
                    root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SpecialistEntity>() {
            override fun areItemsTheSame(
                oldItem: SpecialistEntity,
                newItem: SpecialistEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: SpecialistEntity,
                newItem: SpecialistEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
