package com.mentalys.app.ui.dailytips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalys.app.databinding.ItemDailyTipsBinding

class DailyTipsAdapter(private val items: List<DailyTips>) : RecyclerView.Adapter<DailyTipsAdapter.DailyTipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTipViewHolder {
        val binding = ItemDailyTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyTipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyTipViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class DailyTipViewHolder(private val binding: ItemDailyTipsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyTips) {
            binding.titleTextView.text = item.title
            binding.descTextView.text = item.description
            Glide.with(itemView).load(item.imageResource).into(binding.imageView)
        }
    }

}

data class DailyTips(
    val title: String,
    val description: String,
    val imageResource: Int
)