package com.mentalys.app.ui.mental

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMentalTestResultBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.article.ArticleAdapter
import com.mentalys.app.ui.mental.test.handwriting.MentalTestHandwritingViewModel
import com.mentalys.app.ui.specialist.SpecialistActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class MentalTestResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentalTestResultBinding
    private val viewModel: MentalTestResultViewModel by viewModels {
        ViewModelFactory.getInstance(
            this@MentalTestResultActivity
        )
    }
    private lateinit var articleAdapter: ArticleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleAdapter = ArticleAdapter()
        articleAdapter.setLoadingState(true)

//        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        //Test
        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        val confidencePercentage = intent.getStringExtra(EXTRA_CONFIDENCE_PERCENTAGE)
        val testName = intent.getStringExtra(EXTRA_TEST_NAME)
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val audioUri = intent.getStringExtra(EXTRA_AUDIO_URI)
        val emotionLabel = intent.getStringExtra(EXTRA_EMOTION_LABEL)


        if (testName == "Voice Test") {
            binding.prediction.text = "Test Result : $prediction"
            binding.predictionPercentage.text = "Percentage $confidencePercentage"
        } else if (testName == "Handwriting Test") {
            binding.prediction.text = "Test Result $prediction"
            binding.predictionPercentage.text = "Percentage $confidencePercentage"
        } else {
            binding.prediction.text = "You are indicated to have $prediction"
            binding.predictionPercentage.text = "Percentage $confidencePercentage %"
        }

        if (prediction != null) {
            if (prediction == "Mental Health Condition" || prediction == "Deperession") {
                viewModel.getMentalStateArticle("psychot depresn")
            } else if (prediction == "No Mental Health Condition" || prediction == "NonDepression") {
                binding.consultButton.text = getString(R.string.back_to_home)
                binding.consultButton.setOnClickListener{
                    startActivity(Intent(this, MainActivity::class.java))
                }
                viewModel.getAllArticle()
            } else {
                viewModel.getMentalStateArticle(prediction)
            }
        }

        viewModel.mentalStateArticles.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    articleAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    articleAdapter.setLoadingState(false)
                    articleAdapter.submitList(resource.data)
                    Log.d("Article Retrieved)", resource.data.toString())
                }

                is Resource.Error -> {
                    showToast(this, resource.error)
                }
            }
        }

        binding.articleRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MentalTestResultActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = articleAdapter
        }

        binding.consultButton.setOnClickListener{
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