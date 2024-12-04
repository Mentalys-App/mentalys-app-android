package com.mentalys.app.ui.mental.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mentalys.app.databinding.ItemQuizQuestionBinding

class QuizAdapter(
    private var questions: List<QuizItem>,
    private val onAnswerSelected: (QuizItem, Boolean) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(private val binding: ItemQuizQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quizItem: QuizItem) {
            binding.questionTextView.text = quizItem.questionText

            val customRadioGroup = binding.radioGroup

            // Reset radio buttons
            customRadioGroup.clearCheck()

            // Set previously selected answer if exists
            when (quizItem.selectedAnswer) {
                true -> binding.radioButtonYes.isChecked = true
                false -> binding.radioButtonNo.isChecked = true
                null -> binding.radioGroup.clearCheck()
            }

            customRadioGroup.setOnCheckedChangeListener { _, _ ->
                customRadioGroup.validateSelection()
            }

            // Set listeners
            binding.radioButtonYes.setOnClickListener {
                quizItem.selectedAnswer = true
                onAnswerSelected(quizItem, true)
                customRadioGroup.clearValidationError()
            }

            binding.radioButtonNo.setOnClickListener {
                quizItem.selectedAnswer = false
                onAnswerSelected(quizItem, false)
                customRadioGroup.clearValidationError()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }


    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount() = questions.size
}

data class QuizItem(
    val questionNumber: Int,
    val questionText: String,
    var selectedAnswer: Boolean? = null
)