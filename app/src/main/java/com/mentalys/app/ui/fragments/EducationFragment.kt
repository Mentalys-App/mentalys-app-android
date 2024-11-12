package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentEducationBinding
import com.mentalys.app.ui.adapters.ArticleAdapter
import com.mentalys.app.ui.adapters.ArticleItem
import com.mentalys.app.ui.adapters.CarouselAdapter
import com.mentalys.app.ui.adapters.FoodAdapter
import com.mentalys.app.ui.adapters.FoodItem
import com.mentalys.app.ui.adapters.Item

class EducationFragment : Fragment() {

    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEducationBinding.inflate(layoutInflater, container, false)
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

        val articleItems = listOf(
            ArticleItem(
                R.drawable.image_depression,
                "Breaking Through the Fog: Understanding and Coping with Depression",
                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
                "by Muhammad Ibnu",
                "07 Nov",
                "• 10 minutes read",
                "#depression #stress"
            ),
            ArticleItem(
                R.drawable.image_depression,
                "2Breaking Through the Fog: Understanding and Coping with Depression",
                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
                "by Muhammad Ibnu",
                "07 Nov",
                "• 10 minutes read",
                "#depression #stress"
            ),
            ArticleItem(
                R.drawable.image_depression,
                "3Breaking Through the Fog: Understanding and Coping with Depression",
                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
                "by Muhammad Ibnu",
                "07 Nov",
                "• 10 minutes read",
                "#depression #stress"
            ),
            ArticleItem(
                R.drawable.image_depression,
                "4Breaking Through the Fog: Understanding and Coping with Depression",
                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
                "by Muhammad Ibnu",
                "07 Nov",
                "• 10 minutes read",
                "#depression #stress"
            ),
            ArticleItem(
                R.drawable.image_depression,
                "5Breaking Through the Fog: Understanding and Coping with Depression",
                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
                "by Muhammad Ibnu",
                "07 Nov",
                "• 10 minutes read",
                "#depression #stress"
            ),
        )

        // Set up the article adapter
        val articleAdapter = ArticleAdapter(articleItems)
        binding.articleRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.articleRecyclerView.adapter = articleAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}