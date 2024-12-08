package com.mentalys.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentHomeBinding
import com.mentalys.app.ui.adapters.PsychiatristsAdapter
import com.mentalys.app.ui.adapters.PsychiatristsItem
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast
import android.Manifest
import android.content.pm.PackageManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.Priority
import com.mentalys.app.ui.clinic.ClinicAdapter
import com.mentalys.app.ui.clinic.ClinicViewModel
import com.mentalys.app.ui.dailytips.DailyTips
import com.mentalys.app.ui.dailytips.DailyTipsAdapter
import com.mentalys.app.ui.mental.test.handwriting.MentalTestHandwritingActivity
import com.mentalys.app.ui.mental.test.quiz.MentalTestQuizTestActivity
import com.mentalys.app.ui.mental.test.voice.MentalTestVoiceActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClinicViewModel by viewModels() {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var clinicAdapter: ClinicAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    val carouselItems = listOf(
        DailyTips(
            title = "Mindfulness Tip",
            description = "Take 5 minutes today to focus on your breathing. Inhale deeply for 4 seconds, hold for 4 seconds, and exhale for 4 seconds.",
            imageResource = R.drawable.icon_calm
        ),
        DailyTips(
            title = "Positive Thinking",
            description = "Start your day by saying one positive thing about yourself. It can be a small achievement or a quality you admire.",
            imageResource = R.drawable.icon_happy
        ),
        DailyTips(
            title = "Stress Relief",
            description = "When feeling overwhelmed, take a break and step outside for fresh air. A few minutes in nature can help reset your mind.",
            imageResource = R.drawable.icon_nature
        ),
        DailyTips(
            title = "Gratitude Exercise",
            description = "Write down 3 things you're grateful for today. It can help shift your mindset to a more positive one.",
            imageResource = R.drawable.icon_book
        ),
        DailyTips(
            title = "Physical Activity Tip",
            description = "Try a quick 10-minute stretch session. It helps release tension and boosts mood!",
            imageResource = R.drawable.icon_exercise
        ),
        DailyTips(
            title = "Sleep Hygiene",
            description = "To improve sleep, avoid screens for at least 30 minutes before bed. Let your mind wind down.",
            imageResource = R.drawable.icon_bed
        )
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.questionnaireLayout.setOnClickListener {
            startActivity(Intent(requireContext(), MentalTestQuizTestActivity::class.java))
        }

        binding.voiceLayout.setOnClickListener {
            startActivity(Intent(requireContext(), MentalTestVoiceActivity::class.java))
        }

        binding.handwritingLayout.setOnClickListener {
            startActivity(Intent(requireContext(), MentalTestHandwritingActivity::class.java))
        }

        binding.clinicLayout.setOnClickListener {
            showToast(requireContext(), "clicked")
        }

        Glide.with(requireActivity()).load(R.drawable.icon_banner_music).into(binding.imageView)

        // Tanpa gps
        clinicAdapter = ClinicAdapter()
        clinicAdapter.setLoadingState(true)
        val lat = -8.64947788622037
        val lng = 115.22191012941667
        // Trigger fetching of clinics
        viewModel.getList4Clinics(lat, lng)

        // Observe articles LiveData
        viewModel.clinics.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    clinicAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    clinicAdapter.setLoadingState(false)
                    clinicAdapter.submitList(resource.data)
                    Log.d("Article Retrieved)", resource.data.toString())
                }

                is Resource.Error -> {
                    showToast(requireContext(), resource.error)
                }
            }
        }

        binding.rvNearbyClinics.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = clinicAdapter
        }


        // DENGAN GPS
//        clinicAdapter = ClinicAdapter()
//        clinicAdapter.setLoadingState(true)
//        getCurrentLocation()
//
//        binding.rvNearbyClinics.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//            adapter = clinicAdapter
//        }

//        setupTopMenu()
//        setupSpecialist()
        setupArticleRecyclerView()
        setupDailyTipsRecyclerView()
        ///////////////

    }
    
    private fun setupDailyTipsRecyclerView() {
        // Set up RecyclerView with horizontal layout manager
        binding.dailyTipsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dailyTipsRecyclerView.adapter = DailyTipsAdapter(carouselItems)

        // SnapHelper to enable snapping
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.dailyTipsRecyclerView)

        // Add indicator dots
        addIndicatorDots(carouselItems.size)

        // Initially set the first dot as selected
        updateIndicator(binding.dailyTipsRecyclerView)

        // Listener for scroll state changes
        binding.dailyTipsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                updateIndicator(recyclerView)
            }
        })
    }
    
    private fun addIndicatorDots(count: Int) {
        for (i in 0 until count) {
            val dot = ImageView(requireContext())
            dot.setImageResource(R.drawable.indicator_dot) // Create a drawable for your indicator dot
            val params = LinearLayout.LayoutParams(16, 16)
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            binding.indicatorContainer.addView(dot)
        }
    }

    private fun updateIndicator(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()

        // Update the indicator's selected dot
        for (i in 0 until binding.indicatorContainer.childCount) {
            val dot = binding.indicatorContainer.getChildAt(i) as ImageView
            dot.setImageResource(if (i == position) R.drawable.indicator_dot_selected else R.drawable.indicator_dot)
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    fetchClinics(lat, lng)
                } else {
                    // Default lokasi
                    fetchClinics(-6.200000, 106.816666)
                }
            }.addOnFailureListener { exception ->
                Log.e("LocationError", "Failed to get location: ${exception.message}")
                showToast(requireContext(), "Failed to fetch location.")
            }
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun fetchClinics(lat: Number, lng: Number) {
        viewModel.getList4Clinics(lat, lng)
        viewModel.clinics.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    clinicAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    clinicAdapter.setLoadingState(false)
                    clinicAdapter.submitList(resource.data)
                }

                is Resource.Error -> {
                    clinicAdapter.setLoadingState(false)
                }
            }
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            showToast(requireContext(), "Permission Location Denied")
        }
    }

    private fun setupSpecialist() {
        val psychiatristsItem = listOf(
            PsychiatristsItem(
                R.drawable.img_clinic,
                "Dr Made Agus Buydiartaaaa",
                "08.00 - 18.00",
                "Rp 20.000,00"
            ),
            PsychiatristsItem(
                R.drawable.img_clinic,
                "Dr Made Agus",
                "08.00 - 18.00",
                "Rp 20.000,00"
            ),
            PsychiatristsItem(
                R.drawable.img_clinic,
                "Dr Made Agus",
                "08.00 - 18.00",
                "Rp 20.000,00"
            ),
        )
        val psychiatristsAdapter = PsychiatristsAdapter(psychiatristsItem)
        binding.rvPsychiatrists.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPsychiatrists.adapter = psychiatristsAdapter
    }

    private fun setupTopMenu() {
        Glide.with(this)
            .load(R.drawable.konsultasi_psikiater)
            .transform(CircleCrop())
            .into(binding.iconMenuKonsultasi)
        Glide.with(this)
            .load(R.drawable.klinik_psikologi)
            .transform(CircleCrop())
            .into(binding.iconMenuKlinik)
        Glide.with(this)
            .load(R.drawable.cek_mental_health)
            .transform(CircleCrop())
            .into(binding.iconMenuCekMental)
        Glide.with(this)
            .load(R.drawable.artikel_mental_health)
            .transform(CircleCrop())
            .into(binding.iconMenuBlm)
    }

    private fun setupArticleRecyclerView() {

//        val articleItems = listOf(
//            ArticleItem(
//                R.drawable.image_depression,
//                "Breaking Through the Fog: Understanding and Coping with Depression",
//                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
//                "by Muhammad Ibnu",
//                "07 Nov",
//                "• 10 minutes read",
//                "#depression #stress"
//            ),
//            ArticleItem(
//                R.drawable.image_depression,
//                "2Breaking Through the Fog: Understanding and Coping with Depression",
//                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
//                "by Muhammad Ibnu",
//                "07 Nov",
//                "• 10 minutes read",
//                "#depression #stress"
//            ),
//            ArticleItem(
//                R.drawable.image_depression,
//                "3Breaking Through the Fog: Understanding and Coping with Depression",
//                "Depression can feel overwhelming and isolating, but you're not alone. This article explores common symptoms, coping strategies, and small steps you can take to start feeling better. Discover practical ways to manage depression and find hope through everyday actions.",
//                "by Muhammad Ibnu",
//                "07 Nov",
//                "• 10 minutes read",
//                "#depression #stress"
//            ),
//        )

        // Set up the article adapter
//        val articleAdapter = ArticleAdapter(articleItems)
//        binding.rvInsights.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        binding.rvInsights.adapter = articleAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}