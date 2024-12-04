package com.mentalys.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.data.local.entity.HandwritingTestEntity
import com.mentalys.app.databinding.ItemHistoryTestBinding
import com.mentalys.app.utils.formatTimestamp

class HandwritingTestAdapter :
    PagingDataAdapter<HandwritingTestEntity, HandwritingTestAdapter.HandwritingTestViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandwritingTestViewHolder {
        val binding = ItemHistoryTestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HandwritingTestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HandwritingTestViewHolder, position: Int) {
        val test = getItem(position)
        if (test != null) {
            holder.bind(test)
        }
    }

    class HandwritingTestViewHolder(private val binding: ItemHistoryTestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(test: HandwritingTestEntity) {
            binding.apply {
                tvTestName.text = "Handwriting Test"
                tvTestResult.text = test.predictionResult
                tvTestPercentage.text = "Confidence: ${test.confidencePercentage}"
                tvDate.text = formatTimestamp(test.timestamp)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HandwritingTestEntity>() {
            override fun areItemsTheSame(
                oldItem: HandwritingTestEntity,
                newItem: HandwritingTestEntity
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: HandwritingTestEntity,
                newItem: HandwritingTestEntity
            ): Boolean = oldItem == newItem
        }
    }
}