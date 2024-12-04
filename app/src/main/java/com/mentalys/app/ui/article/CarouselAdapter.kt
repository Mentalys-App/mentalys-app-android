package com.mentalys.app.ui.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mentalys.app.R

class CarouselAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imageView.context)
            .load(item.imageResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
        holder.titleTextView.text = item.title
    }

    override fun getItemCount() = items.size
}

data class Item(val imageResId: Int, val title: String, val description: String)
