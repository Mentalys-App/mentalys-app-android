package com.mentalys.app.ui.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityTestResultBinding

class TestResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        val confidencePercentage = intent.getStringExtra(EXTRA_CONFIDENCE_PERCENTAGE)
        val testName = intent.getStringExtra(EXTRA_TEST_NAME)
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val audioUri = intent.getStringExtra(EXTRA_AUDIO_URI)
        val emotionLabel = intent.getStringExtra(EXTRA_EMOTION_LABEL)

        if (testName == "Voice Test") {
            binding.prediction.text = prediction
            binding.predictionPercentage.text = confidencePercentage
        } else if (testName == "Handwriting Test") {
            binding.prediction.text = prediction
            binding.predictionPercentage.text = confidencePercentage
        } else {
            binding.prediction.text = prediction
            binding.predictionPercentage.text = confidencePercentage
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