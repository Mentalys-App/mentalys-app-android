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
import com.mentalys.app.ui.mental.MentalTestActivity
import com.mentalys.app.ui.adapters.PsychiatristsAdapter
import com.mentalys.app.ui.adapters.PsychiatristsItem
import com.mentalys.app.ui.specialist.SpecialistActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.mentalys.app.ui.clinic.ClinicAdapter
import com.mentalys.app.ui.clinic.ClinicViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        // Go to mental check menu
        binding.topMentalCheckMenu.setOnClickListener {
            val intent = Intent(requireContext(), MentalTestActivity::class.java)
            startActivity(intent)
        }

        binding.topConsultasionMenu.setOnClickListener {
            startActivity(Intent(requireContext(), SpecialistActivity::class.java))
        }

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