package com.mentalys.app.ui.clinic

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.BuildConfig
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.ClinicEntity
import com.mentalys.app.databinding.ItemDataArticleBinding
import com.mentalys.app.databinding.ItemDataClinicBinding
import com.mentalys.app.databinding.ItemShimmerArticleBinding
import com.mentalys.app.databinding.ItemShimmerClinicBinding
import com.mentalys.app.ui.article.ArticleAdapter.MyViewHolder


class ClinicAdapter(

    private var isLoading: Boolean = true,
) : ListAdapter<ClinicEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerClinicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ShimmerViewHolder(binding)
        } else {
            val binding = ItemDataClinicBinding.inflate(
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

    class MyViewHolder(val binding: ItemDataClinicBinding, ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clinic: ClinicEntity) {
            binding.apply {
                tvClinicName.text = clinic.name
                if (clinic.openNow) {
                    tvClinicTime.text = itemView.context.getString(R.string.open)
                    tvClinicTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.primary))
                } else {
                    tvClinicTime.text = itemView.context.getString(R.string.close)
                    tvClinicTime.setTextColor(ContextCompat.getColor(itemView.context, R.color.error_color))
                }
                tvClinicAddress.text = clinic.vicinity
                Glide.with(root.context)
                    .load(clinic.photoUrl)
                    .error(R.drawable.ic_image)
                    // .transform(RoundedCorners(16))
                    .into(imgClinic)

                // Handle Item Click
                itemView.setOnClickListener {
                    try {
                        // Use the clinic's name and vicinity to create a geo URI
                        val gmmIntentUri =
                            Uri.parse("geo:0,0?q=${Uri.encode(clinic.name)},${Uri.encode(clinic.vicinity)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")

                        // Check if Google Maps is installed
                        if (mapIntent.resolveActivity(root.context.packageManager) != null) {
                            root.context.startActivity(mapIntent)
                        } else {
                            val webMapUri = Uri.parse(
                                "https://www.google.com/maps/search/?api=1&query=${
                                    Uri.encode(clinic.name)
                                },${Uri.encode(clinic.vicinity)}"
                            )
                            val webMapIntent = Intent(Intent.ACTION_VIEW, webMapUri)
                            root.context.startActivity(webMapIntent)
                        }
                    } catch (e: Exception) {

                        Log.e("MyViewHolder", "Error opening map: ${e.message}")
                        Toast.makeText(root.context, "Unable to open maps", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return currentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoadingState(isLoading: Boolean) {
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    class ShimmerViewHolder(binding: ItemShimmerClinicBinding) :
        RecyclerView.ViewHolder(binding.root)


    companion object {
        private const val API_KEY = BuildConfig.GOOGLE_MAP_API_KEY
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

