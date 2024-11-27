package com.mentalys.app.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import com.mentalys.app.databinding.FragmentQuizTestPage2Binding
import com.mentalys.app.ui.activities.QuizTestActivity
import com.mentalys.app.ui.viewmodels.QuizTestViewModel


class QuizTestPage2Fragment : Fragment() {
    private val quizViewModel: QuizTestViewModel by activityViewModels()
    private var _binding: FragmentQuizTestPage2Binding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage2Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupQuestionListeners()
        observeAndRestoreAnswers()
        binding.quizPage2BtnNext.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage3Fragment())
        }
        binding.quizPage2BtnBack.setOnClickListener{
            (activity as QuizTestActivity).replaceFragment(QuizTestPage1Fragment())
        }
    }
    private fun setupQuestionListeners() {
        for (questionNumber in 11..20) {
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
                    ) -> "true"

                    resources.getIdentifier(
                        "answer_no$questionNumber",
                        "id",
                        context?.packageName
                    ) -> "false"

                    else -> ""
                }
                quizViewModel.setAnswer(questionNumber, answer)
            }
        }
    }

    private fun observeAndRestoreAnswers() {
        quizViewModel.answers.observe(viewLifecycleOwner) { answers ->
            for (questionNumber in 11..20) {
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