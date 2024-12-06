package com.mentalys.app.ui.mental.test.quiz


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentQuizTestPage2Binding
import com.mentalys.app.ui.custom_views.CustomRadioGroup


class MentalTestQuizPage2Fragment : Fragment() {
    private val quizViewModel: MentalTestQuizViewModel by activityViewModels()
    private var _binding: FragmentQuizTestPage2Binding? = null
    private val binding get() = _binding!!
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var quizQuestions: List<QuizItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareQuizQuestions()
        setupRecyclerView()
        binding.quizPage2BtnNext.setOnClickListener {
            // Flag to track if all validations pass
            var allValidationsPassed = true

            // Validate radio groups and show errors for each unanswered question
            quizQuestions.forEach { question ->
                val radioGroupViewHolder =
                    binding.quizRecyclerView2.findViewHolderForAdapterPosition(question.questionNumber - 11)
                val radioGroup =
                    radioGroupViewHolder?.itemView?.findViewById<CustomRadioGroup>(R.id.radio_group)

                // Validate each radio group and update the flag
                if (radioGroup?.validateSelection() == false) {
                    allValidationsPassed = false
                }
            }

            // Validate age and radio groups
            if (allValidationsPassed) {
                (activity as MentalTestQuizTestActivity).replaceFragment(MentalTestQuizPage3Fragment())
            }
        }
        binding.quizPage2BtnBack.setOnClickListener {
            (activity as MentalTestQuizTestActivity).replaceFragment(MentalTestQuizPage1Fragment())
        }
    }

    private fun prepareQuizQuestions() {
        quizQuestions = listOf(
            QuizItem(11, getString(R.string.question_11)),
            QuizItem(12, getString(R.string.question_12)),
            QuizItem(13, getString(R.string.question_13)),
            QuizItem(14, getString(R.string.question_14)),
            QuizItem(15, getString(R.string.question_15)),
            QuizItem(16, getString(R.string.question_16)),
            QuizItem(17, getString(R.string.question_17)),
            QuizItem(18, getString(R.string.question_18)),
            QuizItem(19, getString(R.string.question_19)),
            QuizItem(20, getString(R.string.question_20)),
        )
    }

    private fun setupRecyclerView() {
        quizQuestions = quizViewModel.getAnswersForQuestions(quizQuestions)

        quizAdapter = QuizAdapter(quizQuestions) { quizItem, answer ->
            quizViewModel.setAnswer(quizItem.questionNumber, answer.toString())
        }

        binding.quizRecyclerView2.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = quizAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}