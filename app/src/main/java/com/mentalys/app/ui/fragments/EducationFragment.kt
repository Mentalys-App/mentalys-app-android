package com.mentalys.app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.carousel.CarouselSnapHelper
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentEducationBinding
import com.mentalys.app.ui.adapters.CarouselAdapter
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
            Item(R.drawable.image_depression, getString(R.string.featured_title_1), getString(R.string.featured_description_1)),
            Item(R.drawable.image_cherries, getString(R.string.featured_title_2), getString(R.string.featured_description_1)),
            Item(R.drawable.image_avocado, getString(R.string.featured_title_3), getString(R.string.featured_description_1)),
        )

        // Set up the carousel adapter
        val adapter = CarouselAdapter(carouselItems)
        val snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(binding.carouselRecyclerView)
        binding.carouselRecyclerView.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}