package com.mentalys.app.ui.clinic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityClinicBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast
import kotlin.math.log

class ClinicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClinicBinding
    private val viewModel: ClinicViewModel by viewModels {
        ViewModelFactory.getInstance(this@ClinicActivity)
    }
    private lateinit var clinicAdapter: ClinicAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Default location (Bali, Indonesia)
    private val DEFAULT_LAT = -6.200000
    private val DEFAULT_LNG = 106.816666


    private val LOCATION_PERMISSION_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClinicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        clinicAdapter = ClinicAdapter()
        clinicAdapter.setLoadingState(true)

        // Check and request location permissions
        if (checkLocationPermissions()) {
            Log.d("List Clinic", "Start Fetch location")
            fetchCurrentLocationAndClinics()
        } else {
            requestLocationPermissions()
        }

        // Observe clinics LiveData
        viewModel.clinics.observe(this) { resource ->
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
                    showToast(this, resource.error)
                }
            }
        }
        fetchCurrentLocationAndClinics()

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@ClinicActivity, LinearLayoutManager.VERTICAL, false)
            adapter = clinicAdapter
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun fetchCurrentLocationAndClinics() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Location", "get Lat Lng")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val latitude = location?.latitude ?: DEFAULT_LAT
                    val longitude = location?.longitude ?: DEFAULT_LNG
                    Log.e("LocationError", "Lat ${latitude}")
                    viewModel.getListClinics(latitude, longitude)
                }
                .addOnFailureListener {
                    viewModel.getListClinics(DEFAULT_LAT, DEFAULT_LNG)
                }
        } else {
            viewModel.getListClinics(DEFAULT_LAT, DEFAULT_LNG)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                fetchCurrentLocationAndClinics()
            } else {
                // Use default location if permissions denied
                viewModel.getListClinics(DEFAULT_LAT, DEFAULT_LNG)
            }
        }
    }
}
