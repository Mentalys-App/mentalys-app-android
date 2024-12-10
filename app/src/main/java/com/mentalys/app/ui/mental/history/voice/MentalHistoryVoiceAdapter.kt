package com.mentalys.app.ui.mental.history.voice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryEntity
import com.mentalys.app.databinding.ItemHistoryTestBinding
import com.mentalys.app.utils.formatTimestamp

class MentalHistoryVoiceAdapter(
//    private var isLoading: Boolean = true,
//    private val items: List<ArticleListItem>
) : ListAdapter<VoiceHistoryEntity, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

//    override fun getItemViewType(position: Int): Int {
//        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == VIEW_TYPE_SHIMMER) {
//            val binding = ItemShimmerHandwritingBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//            ShimmerViewHolder(binding)
//        } else {
        val binding = ItemHistoryTestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) { // && !isLoading) {
            val event = getItem(position)
            holder.bind(event)
        }
    }

//    class ShimmerViewHolder(binding: ItemShimmerHandwritingBinding) :
//        RecyclerView.ViewHolder(binding.root)

    class MyViewHolder(val binding: ItemHistoryTestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(voice: VoiceHistoryEntity) {
            binding.apply {
                tvTestName.text = itemView.context.getString(R.string.voice_test)
                tvTestResult.text = voice.prediction?.result?.category
                tvTestPercentage.text =
                    itemView.context.getString(
                        R.string.confidence,
                        voice.prediction?.result?.supportPercentage.toString()
                    )
                tvDate.text = voice.timestamp?.let { formatTimestamp(it) }
                icon.setImageResource(R.drawable.ic_outline_voice)
            }
        }
    }

//    override fun getItemCount(): Int {
//        return if (isLoading || currentList.isNotEmpty()) 3 else minOf(currentList.size, 3)
//    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun setLoadingState(isLoading: Boolean) {
//        this.isLoading = isLoading
//        notifyDataSetChanged()
//    }

    companion object {
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VoiceHistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: VoiceHistoryEntity,
                newItem: VoiceHistoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: VoiceHistoryEntity,
                newItem: VoiceHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}

//class HandwritingTestAdapter :
//    PagingDataAdapter<HandwritingEntity, HandwritingTestAdapter.HandwritingTestViewHolder>(
//        DIFF_CALLBACK
//    ) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandwritingTestViewHolder {
//        val binding = ItemHistoryTestBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return HandwritingTestViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: HandwritingTestViewHolder, position: Int) {
//        val test = getItem(position)
//        if (test != null) {
//            holder.bind(test)
//        }
//    }
//
//    class HandwritingTestViewHolder(private val binding: ItemHistoryTestBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(test: HandwritingEntity) {
//            binding.apply {
//                tvTestName.text = "Handwriting Test"
//                tvTestResult.text = test.predictionResult
//                tvTestPercentage.text = "Confidence: ${test.confidencePercentage}"
//                tvDate.text = formatTimestamp(test.timestamp)
//            }
//        }
//    }
//
//    companion object {
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HandwritingEntity>() {
//            override fun areItemsTheSame(
//                oldItem: HandwritingEntity,
//                newItem: HandwritingEntity
//            ): Boolean = oldItem.id == newItem.id
//
//            override fun areContentsTheSame(
//                oldItem: HandwritingEntity,
//                newItem: HandwritingEntity
//            ): Boolean = oldItem == newItem
//        }
//    }
//}