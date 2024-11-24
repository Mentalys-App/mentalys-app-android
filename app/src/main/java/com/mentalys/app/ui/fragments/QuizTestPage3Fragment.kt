package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import com.mentalys.app.databinding.FragmentQuizTestPage3Binding
import com.mentalys.app.ui.activities.QuizTestActivity
import com.mentalys.app.ui.viewmodels.QuizTestViewModel

class QuizTestPage3Fragment : Fragment() {
    private val quizViewModel: QuizTestViewModel by activityViewModels()
    private var _binding: FragmentQuizTestPage3Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage3Binding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupQuestionListeners()
        observeAndRestoreAnswers()
        binding.quizPage3BtnBack.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage2Fragment())
        }
    }
    private fun setupQuestionListeners() {
        for (questionNumber in 21..27) {
            val radioGroup = binding.root.findViewById<RadioGroup>(
                resources.getIdentifier(
                    "quiz_test_answer$questionNumber",
                    "id",
                    context?.packageName
                )
            )
            radioGroup?.setOnCheckedChangeListener { _, checkedId ->
                val answer = when (checkedId) {
                    resources.getIdentifier(
                        "answer_yes$questionNumber",
                        "id",
                        context?.packageName
                    ) -> "yes"

                    resources.getIdentifier(
                        "answer_no$questionNumber",
                        "id",
                        context?.packageName
                    ) -> "no"

                    else -> ""
                }
                quizViewModel.setAnswer(questionNumber, answer)
            }
        }
    }

    private fun observeAndRestoreAnswers() {
        quizViewModel.answers.observe(viewLifecycleOwner) { answers ->
            for (questionNumber in 21..27) {
                answers[questionNumber]?.let { answer ->
                    val radioGroup = binding.root.findViewById<RadioGroup>(
                        resources.getIdentifier(
                            "quiz_test_answer$questionNumber",
                            "id",
                            context?.packageName
                        )
                    )
                    when (answer) {
                        "yes" -> radioGroup?.check(
                            resources.getIdentifier(
                                "answer_yes$questionNumber",
                                "id",
                                context?.packageName
                            )
                        )

                        "no" -> radioGroup?.check(
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