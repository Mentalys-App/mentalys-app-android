package com.mentalys.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mentalys.app.R

class PsychiatristsAdapter(private val items: List<PsychiatristsItem>) : RecyclerView.Adapter<PsychiatristsAdapter.PsychiatristsViewHolder>() {

    class PsychiatristsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgPsychiatrists)
        val tvName: TextView = itemView.findViewById(R.id.tvPsychiatristsName)
        val tvTime: TextView = itemView.findViewById(R.id.tvPsychiatristsTime)
        val tvConsultationFee : TextView = itemView.findViewById(R.id.tvConsultationFee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PsychiatristsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_psikolog_home, parent, false)
        return PsychiatristsViewHolder(view)
    }


    override fun onBindViewHolder(holder: PsychiatristsViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imageView.context)
            .load(item.imageResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(CircleCrop())
            .into(holder.imageView)
        holder.tvName.text = item.name
        holder.tvTime.text = item.time
        holder.tvConsultationFee.text = item.price
    }

    override fun getItemCount() = items.size

}

data class PsychiatristsItem(
    val imageResId: Int,
    val name: String,
    val time: String,
    val price: String,
)