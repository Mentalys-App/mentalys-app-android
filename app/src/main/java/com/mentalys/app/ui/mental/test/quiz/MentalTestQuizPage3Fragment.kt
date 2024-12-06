package com.mentalys.app.ui.mental.test.quiz

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.databinding.FragmentQuizTestPage3Binding
import com.mentalys.app.ui.mental.MentalTestResultActivity
import com.mentalys.app.ui.custom_views.CustomRadioGroup
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MentalTestQuizPage3Fragment : Fragment() {
    private val quizViewModel: MentalTestQuizViewModel by activityViewModels()
    private var _binding: FragmentQuizTestPage3Binding? = null
    private val binding get() = _binding!!

    private lateinit var quizAdapter: QuizAdapter
    private lateinit var quizQuestions: List<QuizItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestPage3Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareQuizQuestions()
        setupRecyclerView()
        binding.quizPage3BtnBack.setOnClickListener {
            (activity as MentalTestQuizTestActivity).replaceFragment(MentalTestQuizPage2Fragment())
        }
        binding.sendButton.setOnClickListener {
            // Flag to track if all validations pass
            var allValidationsPassed = true

            // Validate radio groups and show errors for each unanswered question
            quizQuestions.forEach { question ->
                val radioGroupViewHolder =
                    binding.quizRecyclerView3.findViewHolderForAdapterPosition(question.questionNumber - 21)
                val radioGroup =
                    radioGroupViewHolder?.itemView?.findViewById<CustomRadioGroup>(R.id.radio_group)

                // Validate each radio group and update the flag
                if (radioGroup?.validateSelection() == false) {
                    allValidationsPassed = false
                }
            }

            // Validate age and radio groups
            if (allValidationsPassed) {
                viewLifecycleOwner.lifecycleScope.launch {
                    sendAsnwers(SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first())
                }
            }
        }
        setupObservers()
    }

    private fun prepareQuizQuestions() {
        quizQuestions = listOf(
            QuizItem(21, getString(R.string.question_21)),
            QuizItem(22, getString(R.string.question_22)),
            QuizItem(23, getString(R.string.question_23)),
            QuizItem(24, getString(R.string.question_24)),
            QuizItem(25, getString(R.string.question_25)),
            QuizItem(26, getString(R.string.question_26)),
            QuizItem(27, getString(R.string.question_27))
        )
    }

    private fun setupRecyclerView() {
        quizQuestions = quizViewModel.getAnswersForQuestions(quizQuestions)

        quizAdapter = QuizAdapter(quizQuestions) { quizItem, answer ->
            quizViewModel.setAnswer(quizItem.questionNumber, answer.toString())
        }

        binding.quizRecyclerView3.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = quizAdapter
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
        quizViewModel.quizTest(token, quizRequest)
    }

    private fun setupObservers() {
        quizViewModel.testResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(true)
                    quizViewModel.clearAllAnswers()
                    val response = result.data
                    val prediction = response.prediction?.data
                    val confidence = prediction?.confidenceScore
                    val diagnosis = prediction?.diagnosis

                    if (diagnosis != null) {
                        moveToResult(diagnosis, confidence.toString())
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.quizRecyclerView3.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.linearLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun moveToResult(diagnosis: String, confidence: String) {
        val intent = Intent(requireContext(), MentalTestResultActivity::class.java).apply {
            putExtra(MentalTestResultActivity.EXTRA_PREDICTION, diagnosis)
            putExtra(MentalTestResultActivity.EXTRA_CONFIDENCE_PERCENTAGE, confidence)
        }
        startActivity(intent)
        requireActivity().finish()
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