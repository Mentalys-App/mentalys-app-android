package com.mentalys.app.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mentalys.app.R
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.databinding.FragmentQuizTestPage3Binding
import com.mentalys.app.ui.activities.QuizTestActivity
import com.mentalys.app.ui.activities.TestResultActivity
import com.mentalys.app.ui.viewmodels.QuizTestViewModel
import com.mentalys.app.utils.Result

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
        binding.sendButton.setOnClickListener{
            sendAsnwers("")
        }
        setupObservers()
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

    private fun sendAsnwers(token: String) {
        val answers = quizViewModel.answers.value ?: mutableMapOf()
        val quizRequest = MentalTestRepository.QuizRequest(
            age = answers[1] ?: "",
            feeling_nervous = answers[2]?.toBoolean() ?: false,
            panic = answers[3]?.toBoolean() ?: false,
            breathing_rapidly = answers[4]?.toBoolean() ?: false,
            sweating = answers[5]?.toBoolean() ?: false,
            trouble_in_concentration = answers[6]?.toBoolean() ?: false,
            having_trouble_in_sleeping = answers[7]?.toBoolean() ?: false,
            having_trouble_with_work = answers[8]?.toBoolean() ?: false,
            hopelessness = answers[9]?.toBoolean() ?: false,
            anger = answers[10]?.toBoolean() ?: false,
            over_react = answers[11]?.toBoolean() ?: false,
            change_in_eating = answers[12]?.toBoolean() ?: false,
            suicidal_thought = answers[13]?.toBoolean() ?: false,
            feeling_tired = answers[14]?.toBoolean() ?: false,
            close_friend = answers[15]?.toBoolean() ?: false,
            social_media_addiction = answers[16]?.toBoolean() ?: false,
            weight_gain = answers[17]?.toBoolean() ?: false,
            introvert = answers[18]?.toBoolean() ?: false,
            popping_up_stressful_memory = answers[19]?.toBoolean() ?: false,
            having_nightmares = answers[20]?.toBoolean() ?: false,
            avoids_people_or_activities = answers[21]?.toBoolean() ?: false,
            feeling_negative = answers[22]?.toBoolean() ?: false,
            trouble_concentrating = answers[23]?.toBoolean() ?: false,
            blaming_yourself = answers[24]?.toBoolean() ?: false,
            hallucinations = answers[25]?.toBoolean() ?: false,
            repetitive_behaviour = answers[26]?.toBoolean() ?: false,
            seasonally = answers[27]?.toBoolean() ?: false,
            increased_energy = answers[28]?.toBoolean() ?: false

        )
        quizViewModel.quizTest(token,quizRequest)
    }

    private fun setupObservers() {
        quizViewModel.testResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(true)
                    val response = result.data
                    val prediction = response.prediction?.data
                    val confidence = prediction?.confidenceScore
                    val diagnosis = prediction?.diagnosis

                    if (diagnosis != null) {
                        moveToResult(diagnosis, confidence.toString())
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.layoutQuizTest.visibility = if (isLoading) View.GONE else View.VISIBLE

    }

    private fun moveToResult(diagnosis: String, confidence: String) {
        val intent = Intent(requireContext(), TestResultActivity::class.java).apply {
            putExtra(TestResultActivity.EXTRA_PREDICTION, diagnosis)
            putExtra(TestResultActivity.EXTRA_CONFIDENCE_PERCENTAGE, confidence)
        }
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}