package com.mentalys.app.ui.article

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityArticleDetailBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleDetailBinding
    private lateinit var contentAdapter: ContentAdapter
    private val viewModel: ArticleViewModel by viewModels { ViewModelFactory.getInstance(this@ArticleDetailActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.shareButton.setOnClickListener {
            showToast(this@ArticleDetailActivity, "Share clicked")
        }

        // Initialize RecyclerView adapter
        contentAdapter = ContentAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ArticleDetailActivity)
            adapter = contentAdapter
        }

        // Retrieve the article ID from the Intent
        val articleId = intent.getStringExtra("EXTRA_ARTICLE_ID") ?: return // Ensure it's not null

        // Observe ViewModel data
        viewModel.article.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    val article = result.data
                    contentAdapter.submitList(article.content)
                    generateTags(article.metadata?.tags)
                    binding.titleTextView.text = article.title
                    binding.authorTextView.text = "by ${article.author?.name}"
                    binding.dateTextView.text = article.metadata?.publishDate?.let {
                        convertDateToFormattedString(it)
                    }
                    Glide.with(this)
                        .load(article.metadata?.imageLink)
                        .error(R.drawable.image_placeholder)
                        .into(binding.imageView)
                }

                is Resource.Error -> {
                    hideLoading()
                    showToast(this, "Failed to load article")
                }
            }
        }

        // Fetch the article using the ViewModel
        viewModel.getArticle(articleId) // Pass the article ID to the ViewModel

    }

    private fun generateTags(tags: List<String?>?) {
        // Get reference to the ChipGroup from your layout
        val chipGroup = binding.chipGroup

        // Clear existing chips before adding new ones
        chipGroup.removeAllViews()

        // Dynamically create chips
        tags?.forEach { tag ->
            val chip = Chip(this)
            chip.text = "#$tag"
            chip.chipStrokeWidth = 0f
            chip.setTextColor(resources.getColor(R.color.white, theme))
            chip.setChipBackgroundColorResource(R.color.primary)
            chip.shapeAppearanceModel.withCornerSize(16.0F)
            chip.chipCornerRadius = 16.0f
            chipGroup.addView(chip)
        }

    }


    private fun convertDateToFormattedString(isoDate: String): String {
        return try {
            // Parse ISO 8601 date
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC timezone
            val date = isoFormat.parse(isoDate) // Convert to Date object

            // Format to desired style
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            "Invalid Date" // Fallback in case of an error
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

}