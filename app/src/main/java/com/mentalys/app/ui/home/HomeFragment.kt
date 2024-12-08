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
import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Priority
import com.mentalys.app.ui.clinic.ClinicActivity
import com.mentalys.app.ui.clinic.ClinicAdapter
import com.mentalys.app.ui.clinic.ClinicViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClinicViewModel by viewModels() {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var clinicAdapter: ClinicAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val DEFAULT_LAT = -6.200000
    private val DEFAULT_LNG = 106.816666

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                fetchCurrentLocationAndClinics()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                fetchCurrentLocationAndClinics()
            }
            else -> {
                // Use default location if permissions denied
                viewModel.getList4Clinics(DEFAULT_LAT, DEFAULT_LNG)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())


        // Setup click listeners and other initializations
        setupClickListeners()
        setupClinicAdapter()
        setupObservers()
        setupTopMenu()
        setupSpecialist()

        // Check and request location permissions
        if (checkLocationPermissions()) {
            fetchCurrentLocationAndClinics()
        } else {
            requestLocationPermissions()
        }

        if (isGpsEnabled()){
            fetchCurrentLocationAndClinics()
        }else{
            viewModel.getList4Clinics(DEFAULT_LAT, DEFAULT_LNG)
        }

    }

    private fun setupClickListeners() {
        binding.topMentalCheckMenu.setOnClickListener {
            startActivity(Intent(requireContext(), MentalTestActivity::class.java))
        }

        binding.topConsultasionMenu.setOnClickListener {
            startActivity(Intent(requireContext(), SpecialistActivity::class.java))
        }

        binding.tvViewAllClinics.setOnClickListener {
            startActivity(Intent(requireContext(), ClinicActivity::class.java))
        }
    }
    private fun isGpsEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun setupClinicAdapter() {
        clinicAdapter = ClinicAdapter()
        clinicAdapter.setLoadingState(true)
        binding.rvNearbyClinics.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = clinicAdapter
        }
    }


    private fun setupObservers() {
        viewModel.clinics.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    clinicAdapter.setLoadingState(true)
                }
                is Resource.Success -> {
                    clinicAdapter.setLoadingState(false)
                    clinicAdapter.submitList(resource.data)
                    Log.d("Clinics Retrieved", resource.data.toString())
                }
                is Resource.Error -> {
                    showToast(requireContext(), resource.error)
                }
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun fetchCurrentLocationAndClinics() {
        if (checkLocationPermissions()) {
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        val latitude = location?.latitude ?: DEFAULT_LAT
                        val longitude = location?.longitude ?: DEFAULT_LNG
                        Log.e("HomeFragment", "Lat ${latitude}")
                        viewModel.getList4Clinics(latitude, longitude)
                    }
                    .addOnFailureListener {
                        viewModel.getList4Clinics(DEFAULT_LAT, DEFAULT_LNG)
                    }
            } catch (securityException: SecurityException) {
                viewModel.getList4Clinics(DEFAULT_LAT, DEFAULT_LNG)
            }
        } else {
            viewModel.getList4Clinics(DEFAULT_LAT, DEFAULT_LNG)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        if (isGpsEnabled()){
            fetchCurrentLocationAndClinics()
        }else{
            viewModel.getList4Clinics(DEFAULT_LAT, DEFAULT_LNG)
        }
    }
}