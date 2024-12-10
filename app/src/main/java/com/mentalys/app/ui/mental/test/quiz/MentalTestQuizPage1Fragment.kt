package com.mentalys.app.ui.mental.test.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentQuizTestPage1Binding
import com.mentalys.app.ui.custom_views.CustomRadioGroup
import com.mentalys.app.ui.viewmodels.ViewModelFactory

class MentalTestQuizPage1Fragment : Fragment() {

    private val quizViewModel: MentalTestQuizViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentQuizTestPage1Binding? = null
    private val binding get() = _binding!!
    private lateinit var quizAdapter: QuizAdapter
    private lateinit var quizQuestions: List<QuizItem>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizTestPage1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQuestion1Listeners()
        observeAndRestoreAnswers1()
        prepareQuizQuestions()
        setupRecyclerView()

        binding.backButton.setOnClickListener { requireActivity().finish() }

        binding.quizPage1BtnNext.setOnClickListener {
            val isAgeValid = binding.quizTestAnswer1.text.toString().toIntOrNull()?.let { age ->
                age in 0..150
            } ?: false
            if (!isAgeValid) {
                binding.quizTestAnswer1.error = getString(R.string.age_error_exceed)
            }

            val ageNotEmpty = binding.quizTestAnswer1.text.toString().isNotEmpty()

            if (!ageNotEmpty) {
                binding.quizTestAnswer1.error = getString(R.string.age_error_empty)
            }

            var allValidationsPassed = true

            quizQuestions.forEach { question ->
                val radioGroupViewHolder =
                    binding.quizRecyclerView1.findViewHolderForAdapterPosition(question.questionNumber - 2)
                val radioGroup =
                    radioGroupViewHolder?.itemView?.findViewById<CustomRadioGroup>(R.id.radio_group)
                if (radioGroup?.validateSelection() == false) {
                    allValidationsPassed = false
                }
            }

            if (ageNotEmpty && isAgeValid && allValidationsPassed) {
                (activity as MentalTestQuizTestActivity).replaceFragment(MentalTestQuizPage2Fragment())
            }
        }
    }

    private fun setupQuestion1Listeners() {
        binding.quizTestAnswer1.addTextChangedListener { editable ->
            quizViewModel.setAnswer(1, editable.toString())
        }
    }

    private fun observeAndRestoreAnswers1() {
        quizViewModel.answers.observe(viewLifecycleOwner) { answers ->
            answers[1]?.let { answer ->
                if (binding.quizTestAnswer1.text.toString() != answer) {
                    binding.quizTestAnswer1.setText(answer)
                }
            }
        }
    }

    private fun prepareQuizQuestions() {
        quizQuestions = listOf(
            QuizItem(2, getString(R.string.question_2)),
            QuizItem(3, getString(R.string.question_3)),
            QuizItem(4, getString(R.string.question_4)),
            QuizItem(5, getString(R.string.question_5)),
            QuizItem(6, getString(R.string.question_6)),
            QuizItem(7, getString(R.string.question_7)),
            QuizItem(8, getString(R.string.question_8)),
            QuizItem(9, getString(R.string.question_9)),
            QuizItem(10, getString(R.string.question_10)),
        )
    }

    private fun setupRecyclerView() {
        quizQuestions = quizViewModel.getAnswersForQuestions(quizQuestions)

        quizAdapter = QuizAdapter(quizQuestions) { quizItem, answer ->
            quizViewModel.setAnswer(quizItem.questionNumber, answer.toString())
        }

        binding.quizRecyclerView1.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = quizAdapter
        }
    }


    private fun observeAndRestoreAnswers() {
        quizViewModel.answers.observe(viewLifecycleOwner) { answers ->
            for (questionNumber in 2..10) {
                answers[questionNumber]?.let { answer ->
                    val radioGroup = binding.root.findViewById<RadioGroup>(
                        resources.getIdentifier(
                            "quiz_test_answer$questionNumber",
                            "id",
                            context?.packageName
                        )
                    )
                    when (answer) {
                        "true" -> radioGroup?.check(
                            resources.getIdentifier(
                                "answer_yes$questionNumber",
                                "id",
                                context?.packageName
                            )
                        )

                        "false" -> radioGroup?.check(
                            resources.getIdentifier(
                                "answer_no$questionNumber",
                                "id",
                                context?.packageName
                            )
                        )

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}