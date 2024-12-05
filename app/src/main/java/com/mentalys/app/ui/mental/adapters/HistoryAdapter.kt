package com.mentalys.app.ui.mental.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.R
import com.mentalys.app.data.remote.response.mental.AudioResult
import com.mentalys.app.data.remote.response.mental.HandwritingResult
import com.mentalys.app.data.remote.response.mental.HistoryItem
import com.mentalys.app.data.remote.response.mental.QuizResult
import com.mentalys.app.databinding.ItemHistoryTestBinding
import com.mentalys.app.utils.formatTimestamp

class HistoryAdapter : ListAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemHistoryTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(private val binding: ItemHistoryTestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.tvTestName.text = item.type
            binding.tvDate.text = formatTimestamp(item.timestamp)
            when (val result = item.prediction.result) {
                is AudioResult -> {
                    binding.icon.setImageResource(R.drawable.ic_outline_voice)
                    binding.tvTestName.text = "Voice Test"
                    binding.tvTestResult.text = "Test Result : ${result.category}"
                    binding.tvTestPercentage.text = "Confidence: ${result.support_percentage}%"
                }

                is HandwritingResult -> {
                    binding.tvTestName.text = "Handwriting Test"
                    binding.tvTestResult.text = "Test Result: ${result.result}"
                    binding.tvTestPercentage.text = "Confidence: ${result.confidence_percentage}"
                }

                is QuizResult -> {
                    binding.icon.setImageResource(R.drawable.ic_list)
                    binding.tvTestName.text = "Quiz Test"
                    binding.tvTestResult.text = "Test Result Diagnosis: ${result.diagnosis}"
                    binding.tvTestPercentage.text = "Confidence: ${result.confidence_score}%"
                }

                else -> {
                    binding.tvTestResult.text = "Unknown result"
                    binding.tvTestPercentage.text = ""
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryItem,
                newItem: HistoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}