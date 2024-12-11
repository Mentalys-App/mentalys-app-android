package com.mentalys.app.ui.mental.history

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
import kotlinx.coroutines.withContext

class MentalHistoryAdapter : ListAdapter<HistoryItem, MentalHistoryAdapter.HistoryViewHolder>(
    DIFF_CALLBACK
) {
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
                    binding.tvTestName.text = itemView.context.getString(R.string.voice_test)
                    binding.tvTestResult.text =
                        itemView.context.getString(R.string.test_result2, result.category)
                    binding.tvTestPercentage.text =
                        itemView.context.getString(R.string.confidence2, result.support_percentage.toString())
                }

                is HandwritingResult -> {
                    binding.tvTestName.text = itemView.context.getString(R.string.handwriting_test)
                    binding.tvTestResult.text =
                        itemView.context.getString(R.string.test_result2, result.result)
                    binding.tvTestPercentage.text =
                        itemView.context.getString(R.string.confidence3, result.confidence_percentage)
                }

                is QuizResult -> {
                    binding.icon.setImageResource(R.drawable.ic_list)
                    binding.tvTestName.text = itemView.context.getString(R.string.quiz_check)
                    binding.tvTestResult.text =
                        itemView.context.getString(R.string.test_result2, result.diagnosis)
                    binding.tvTestPercentage.text =
                        itemView.context.getString(R.string.confidence2, result.confidence_score.toString())
                }

                else -> {
                    binding.tvTestResult.text = itemView.context.getString(R.string.unknown_result)
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