package com.mentalys.app.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityAllArticleBinding
import com.mentalys.app.databinding.ActivityAllFoodsBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class AllFoodsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllFoodsBinding
    private lateinit var foodAdapter: FoodAdapter
    private val viewModel: ArticleViewModel by viewModels { ViewModelFactory.getInstance(this@AllFoodsActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllFoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        foodAdapter = FoodAdapter()
        foodAdapter.setLoadingState(true)

        viewModel.getFoods()

        viewModel.foods.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    foodAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    foodAdapter.setLoadingState(false)
                    foodAdapter.submitList(resource.data)
                    Log.d("Food Retrieved)", resource.data.toString())
                }

                is Resource.Error -> {
                    showToast(this, resource.error)
                }
            }
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = foodAdapter
        }

        binding.backButton.setOnClickListener { finish() }
    }
}