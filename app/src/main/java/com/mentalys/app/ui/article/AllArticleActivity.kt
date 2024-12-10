package com.mentalys.app.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityAllArticleBinding
import com.mentalys.app.databinding.ActivityArticleDetailBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class AllArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllArticleBinding
    private lateinit var articleAdapter: ArticleAdapter
    private val viewModel: ArticleViewModel by viewModels { ViewModelFactory.getInstance(this@AllArticleActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        articleAdapter = ArticleAdapter()
        articleAdapter.setLoadingState(true)

        // Trigger fetching of articles
        viewModel.getListArticle()

        viewModel.articles.observe(this) { resource ->
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

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllArticleActivity, LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }
        binding.backButton.setOnClickListener { finish() }
    }
}