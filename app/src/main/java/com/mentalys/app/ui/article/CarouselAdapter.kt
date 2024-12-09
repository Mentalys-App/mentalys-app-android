package com.mentalys.app.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mentalys.app.databinding.ItemCarouselBinding

class CarouselAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            // Load the image
            Glide.with(imageView.context)
                .load(item.imageResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)

            // Set the title
            // titleTextView.text = item.title
        }
    }

    override fun getItemCount() = items.size
}

data class Item(val imageResId: Int)//, val title: String, val description: String)
