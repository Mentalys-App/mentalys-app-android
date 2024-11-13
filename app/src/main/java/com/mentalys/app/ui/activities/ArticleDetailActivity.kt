package com.mentalys.app.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityArticleDetailBinding
import com.mentalys.app.ui.adapters.ContentAdapter
import com.mentalys.app.ui.viewmodels.ArticleViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

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

        // Initialize ListView adapter
        contentAdapter = ContentAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ArticleDetailActivity)
            adapter = contentAdapter
        }

        // Observe ViewModel data
        viewModel.article.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    val contentList = result.data.flatMap { article -> article.contentEntity }
                    contentAdapter.submitList(contentList)
                }
                is Resource.Error -> {
                    hideLoading()
                    showToast(this, "Failed to load article")
                }
            }
        }

        // Initiate data fetch
        viewModel.fetchArticle()

    }


    private fun showLoading() {

    }

    private fun hideLoading() {

    }

}