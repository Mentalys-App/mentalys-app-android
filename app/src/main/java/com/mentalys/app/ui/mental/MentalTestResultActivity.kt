package com.mentalys.app.ui.mental

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMentalTestResultBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.article.ArticleAdapter
import com.mentalys.app.ui.specialist.SpecialistActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class MentalTestResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentalTestResultBinding
    private val viewModel: MentalTestResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var articleAdapter: ArticleAdapter

    // Data class untuk menyimpan informasi hasil tes
    data class TestResult(
        var prediction: String?,
        val confidencePercentage: String?,
        val testName: String?,
        val imageUri: String? = null,
        val audioUri: String? = null,
        val emotionLabel: String? = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val testResult = extractTestResult()
        configureTestResultUI(testResult)
        setupArticleRecyclerView()
        observeArticles()
        configureConsultButton()
    }


    private fun extractTestResult(): TestResult {
        return TestResult(
            prediction = intent.getStringExtra(EXTRA_PREDICTION)
                ?: "ADHD", // Hardcoded for test
            confidencePercentage = intent.getStringExtra(EXTRA_CONFIDENCE_PERCENTAGE),
            testName = intent.getStringExtra(EXTRA_TEST_NAME),
            imageUri = intent.getStringExtra(EXTRA_IMAGE_URI),
            audioUri = intent.getStringExtra(EXTRA_AUDIO_URI),
            emotionLabel = intent.getStringExtra(EXTRA_EMOTION_LABEL)
        )
    }


    private fun configureTestResultUI(testResult: TestResult) {
        var prediction = testResult.prediction ?: return
        val percentage = testResult.confidencePercentage


        binding.prediction.text = getString(R.string.prediction_text, prediction)
        binding.predictionPercentage.text = getString(R.string.prediction_percentage, percentage)


        when (prediction) {
            "Potential Mental Health Condition", "Depression" -> {
                setupMentalHealthConditionUI()
            }

            "No Mental Health Condition", "NonDepression" -> {
                setupNoMentalHealthConditionUI()
            }

            else -> {
                setupCustomPredictionUI(prediction)
            }
        }
    }

    private fun setupMentalHealthConditionUI() {
        binding.encourage.text = getString(R.string.encouragement_mental_health)
        binding.predictionExplanation.visibility = View.GONE
        viewModel.getMentalStateArticle("psychot depresn")
    }

    private fun setupNoMentalHealthConditionUI() {
        binding.encourage.text = getString(R.string.encouragement_no_mental_issues)
        binding.predictionExplanation.visibility = View.GONE
        binding.consultButton.text = getString(R.string.back_to_home)
        binding.consultButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        viewModel.getAllArticle()
    }


    private fun setupCustomPredictionUI(prediction: String) {
        val mentalState = when (prediction) {
            "psychot depresn" -> prediction.replace(" ", "_")
            "sleep disord" -> prediction.replace(" ", "_")
            else -> prediction
        }
        Log.d("Mental State dsfsfsdfs", mentalState)
        val encourageResId =
            resources.getIdentifier("encouragement_$mentalState", "string", packageName)
        binding.encourage.text = getString(encourageResId)

        val explanationResId =
            resources.getIdentifier("explanation_$mentalState", "string", packageName)

        binding.predictionExplanation.text = getString(explanationResId)

        viewModel.getMentalStateArticle(prediction)
    }


    private fun setupArticleRecyclerView() {
        articleAdapter = ArticleAdapter()
        articleAdapter.setLoadingState(true)

        binding.articleRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MentalTestResultActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = articleAdapter
        }
    }

    // Metode untuk mengobservasi artikel dari ViewModel
    private fun observeArticles() {
        viewModel.mentalStateArticles.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> articleAdapter.setLoadingState(true)
                is Resource.Success -> {
                    articleAdapter.setLoadingState(false)
                    articleAdapter.submitList(resource.data)
                    Log.d("Article Retrieved", resource.data.toString())
                }

                is Resource.Error -> showToast(this, resource.error)
            }
        }
    }

    // Metode untuk mengonfigurasi tombol konsultasi
    private fun configureConsultButton() {
        binding.consultButton.setOnClickListener {
            startActivity(Intent(this, SpecialistActivity::class.java))
        }
    }

    companion object {
        const val EXTRA_TEST_NAME = "extra_test_name"
        const val EXTRA_PREDICTION = "extra_label"
        const val EXTRA_CONFIDENCE_PERCENTAGE = "extra_confidence_percentage"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_AUDIO_URI = "extra_audio_uri"
        const val EXTRA_EMOTION_LABEL = "extra_emotion_label"
    }
}