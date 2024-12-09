package com.mentalys.app.ui.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentArticleBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carouselItems = listOf(
            Item(R.drawable.image_carousel_1),
            Item(R.drawable.image_carousel_2),
            Item(R.drawable.image_carousel_3),
            Item(R.drawable.image_carousel_4),
            Item(R.drawable.image_carousel_5),
        )

        // Set up the carousel adapter
        val carouselAdapter = CarouselAdapter(carouselItems)
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
        binding.carouselRecyclerView.adapter = carouselAdapter

        // ============================== FOODS ============================== //
        foodAdapter = FoodAdapter()
        foodAdapter.setLoadingState(true)

        // Trigger fetching of foods
        viewModel.getFoods()

        // Observe foods LiveData
        viewModel.foods.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    foodAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    foodAdapter.setLoadingState(false)
                    foodAdapter.submitList(resource.data)
                    Log.d("requireCfdafasfaontext()", resource.data.toString())
                }

                is Resource.Error -> {
                    showToast(requireContext(), resource.error)
                    Log.d("requireContextrerew()", "resource.data.toString()")
                }
            }
        }

        binding.foodRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = foodAdapter
        }

        // ============================== ARTICLES ============================== //
        articleAdapter = ArticleAdapter()
        articleAdapter.setLoadingState(true)

        // Trigger fetching of articles
        viewModel.getListArticle()

        // Observe articles LiveData
        viewModel.articles.observe(viewLifecycleOwner) { resource ->
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
                    showToast(requireContext(), resource.error)
                }
            }
        }

        binding.articleRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = articleAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}