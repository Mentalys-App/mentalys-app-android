package com.mentalys.app.ui.specialist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.databinding.ItemDataSpecialistBinding
import com.mentalys.app.databinding.ItemShimmerSpecialistBinding
import java.text.NumberFormat
import java.util.Locale

class SpecialistAdapter(
    private var isLoading: Boolean = true,
    // private val items: List<ArticleListItem>
) : ListAdapter<SpecialistEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerSpecialistBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemDataSpecialistBinding.inflate(
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


    class ShimmerViewHolder(binding: ItemShimmerSpecialistBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MyViewHolder(val binding: ItemDataSpecialistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(specialist: SpecialistEntity) {
            binding.apply {
                Glide.with(itemView.context).load(specialist.photoUrl).into(specialistImageView)
                specialistNameTextView.text = specialist.fullName
                specialistSpecialityTextView.text = specialist.mainRole

                // Fee
                val fee = specialist.consultationFee
                val formattedFee = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
                    maximumFractionDigits = 0
                }.format(fee)
                specialistFeeTextView.text = formattedFee

                // Availability
                val firstWorkingHour = specialist.workingHours?.firstOrNull()
                val formattedTime = if (firstWorkingHour != null) {
                    "${firstWorkingHour.startTime?.substringBefore(" ")} - ${
                        firstWorkingHour.endTime?.substringBefore(
                            " "
                        )
                    }"
                } else {
                    "No available time"
                }
                specialistTimeTextView.text = formattedTime

//                itemView.setOnClickListener {
//                    val intent = Intent(root.context, ArticleDetailActivity::class.java)
//                    intent.putExtra("EXTRA_SPECIALIST_ID", specialist.id)
//                    root.context.startActivity(intent)
//                }
            }
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