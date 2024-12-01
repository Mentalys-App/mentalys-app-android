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
import com.mentalys.app.ui.adapters.CarouselAdapter
import com.mentalys.app.ui.adapters.FoodAdapter
import com.mentalys.app.ui.adapters.FoodItem
import com.mentalys.app.ui.adapters.Item
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
            Item(
                R.drawable.image_depression,
                getString(R.string.featured_title_1),
                getString(R.string.featured_description_1)
            ),
            Item(
                R.drawable.image_cherries,
                getString(R.string.featured_title_2),
                getString(R.string.featured_description_1)
            ),
            Item(
                R.drawable.image_avocado,
                getString(R.string.featured_title_3),
                getString(R.string.featured_description_1)
            ),
        )

        // Set up the carousel adapter
        val carouselAdapter = CarouselAdapter(carouselItems)
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
        binding.carouselRecyclerView.adapter = carouselAdapter

        val foodItems = listOf(
            FoodItem(
                R.drawable.image_cherries,
                "Cherries",
                getString(R.string.featured_description_2)
            ),
            FoodItem(
                R.drawable.image_avocado,
                "Avocado",
                getString(R.string.featured_description_3)
            ),
            FoodItem(
                R.drawable.image_avocado,
                "Avocado",
                getString(R.string.featured_description_3)
            ),
            FoodItem(
                R.drawable.image_cherries,
                "Cherries",
                getString(R.string.featured_description_2)
            ),
        )

        // Set up the article adapter
        val foodAdapter = FoodAdapter(foodItems)
        binding.foodRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.foodRecyclerView.adapter = foodAdapter

        articleAdapter = ArticleAdapter()


        // Trigger fetching of articles
        viewModel.getListArticle()

        // Observe articles LiveData
        viewModel.articles.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading state, e.g., ProgressBar
                }
                is Resource.Success -> {
                    // Update adapter with new data
                    articleAdapter.submitList(resource.data)
                    Log.d("requireContext()", resource.data.toString())
                }
                is Resource.Error -> {
                    // Handle error state, e.g., show a Toast or SnackBar
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