package com.mentalys.app.ui.reachout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.R

class ReachOutAdapter(
    private val emotions: List<String>,
    private val onEmotionClick: (String) -> Unit
) : RecyclerView.Adapter<ReachOutAdapter.FeelingViewHolder>() {

    inner class FeelingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.state_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeelingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_state, parent, false)
        return FeelingViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeelingViewHolder, position: Int) {
        val feeling = emotions[position]
        holder.textView.text = feeling
        holder.textView.setOnClickListener { onEmotionClick(feeling) }
    }

    override fun getItemCount() = emotions.size
}
