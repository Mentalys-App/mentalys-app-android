package com.mentalys.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mentalys.app.R

class ClinicAdapter(private val items: List<ClinicItem>) : RecyclerView.Adapter<ClinicAdapter.ClinicViewHolder>() {

    class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgClinic)
        val tvName: TextView = itemView.findViewById(R.id.tvClinicName)
        val tvTime: TextView = itemView.findViewById(R.id.tvClinicTime)
        val tvAddress: TextView = itemView.findViewById(R.id.tvClinicAddress)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClinicAdapter.ClinicViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_klinik_terdekat, parent, false)
        return ClinicViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClinicAdapter.ClinicViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imageView.context)
            .load(item.imageResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)
        holder.tvName.text = item.name
        holder.tvTime.text = item.time
        holder.tvAddress.text = item.address
    }

    override fun getItemCount() = items.size
}

data class ClinicItem(
    val imageResId: Int,
    val name: String,
    val time: String,
    val address: String,
)