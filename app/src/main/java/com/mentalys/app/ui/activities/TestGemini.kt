package com.mentalys.app.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityTestGeminiBinding
import com.mentalys.app.ui.viewmodels.GeminiViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast
import io.noties.markwon.Markwon

class TestGemini : AppCompatActivity() {

    private lateinit var binding: ActivityTestGeminiBinding
    private val viewModel: GeminiViewModel by viewModels {
        ViewModelFactory.getInstance(this@TestGemini)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTestGeminiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.progressBar.visibility = View.VISIBLE
        val markwon = Markwon.create(this)
        val prompt = intent.getStringExtra(EXTRA_PROMPT)
        if (prompt != null) {
            viewModel.generateMentalHealthTips(prompt)
        } else {
            showToast(this, getString(R.string.something_went_wrong))
        }

        // Observe the LiveData from ViewModel
        viewModel.tips.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Log.d("TestGemini", "Loading...")
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    Log.d("TestGemini", "Success: ${resource.data}")
                    markwon.setMarkdown(binding.textView, resource.data)
                    binding.progressBar.visibility = View.GONE
                }

                is Resource.Error -> {
                    Log.e("TestGemini", "Error: ${resource.error}")
                    binding.textView.text = resource.error ?: "An error occurred."
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    companion object {
        const val EXTRA_PROMPT = "EXTRA_PROMPT"
    }

}