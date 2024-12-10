package com.mentalys.app.ui.specialist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mentalys.app.BuildConfig
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivitySpecialistDetailBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SpecialistDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpecialistDetailBinding
    private val viewModel: SpecialistViewModel by viewModels { ViewModelFactory.getInstance(this@SpecialistDetailActivity) }

    // Default map coordinates to be used before the data is loaded
    private val defaultLat = 39.0392
    private val defaultLon = 125.7625
    private var mapUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpecialistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.specialistBookButton.setOnClickListener {
            lifecycleScope.launch {
                val isLogin = SettingsPreferences.getInstance(dataStore).getIsLoginSetting().first()
                if (isLogin) {
                    // todo : payment
                } else {
                    showToast(this@SpecialistDetailActivity, "Please login to use this feature")
                }
            }
        }

        binding.specialistChatButton.setOnClickListener {
            val specialistPhoneNumber = "62895610266456"
            val templateMessage =
                "Hello, I would like to discuss my mental health test results with you. Please let me know how we can proceed."
            redirectToWhatsApp(specialistPhoneNumber, templateMessage)
        }

        // Initialize the map URL with default coordinates (before data is loaded)
        updateMapUrl(defaultLat, defaultLon)

        // Set click listener for the map image
        binding.specialistMapsImageView.setOnClickListener {
            // Open the Google Maps link with the current location (either default or updated)
            openGoogleMaps(mapUrl)
        }

        // Retrieve the specialist ID from the Intent
        val specialistId = intent.getStringExtra(EXTRA_SPECIALIST_ID) ?: return // Ensure it's not null

        // Observe ViewModel data
        viewModel.specialist.observe(this@SpecialistDetailActivity) { result ->
            when (result) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    val specialist = result.data
                    binding.apply {
                        specialistNameTextView.text = specialist.fullName
                        specialistMainRoleTextView.text = specialist.mainRole
                        specialistRatingsTextView.text = specialist.ratings.toString()
                        specialistReviewsTextView.text = "(${specialist.reviewCount} reviews)"
                        specialistCenterPatientsTextView.text = "${specialist.patientsCount.toString()}+"
                        specialistCenterYearsTextView.text = "${specialist.experienceYears}+"
                        specialistCenterRatingsTextView.text = specialist.ratings.toString()
                        specialistCenterReviewsTextView.text = "${specialist.reviewCount}+"
                        specialistAboutMeTextView.text = specialist.aboutMe

                        // Photo
                        Glide.with(this@SpecialistDetailActivity)
                            .load(specialist.photoUrl)
                            .error(R.drawable.image_specialist_placeholder)
                            .placeholder(R.drawable.image_specialist_placeholder)
                            .into(specialistPhotoImageView)

                        // Static Image of Google Maps
                        val lat = specialist.location?.latitude?.toDouble() ?: defaultLat
                        val lon = specialist.location?.longitude?.toDouble() ?: defaultLon
                        updateMapUrl(lat, lon)
                    }
                }

                is Resource.Error -> {
                    hideLoading()
                    // showToast(this@SpecialistDetailActivity, "Failed to load article")
                }
            }
        }

        // Fetch the article using the ViewModel
        viewModel.getSpecialist(specialistId) // Pass the article ID to the ViewModel

    }

    private fun updateMapUrl(lat: Double, lon: Double) {
        // Build the Google Maps Static Map URL
        mapUrl = "https://www.google.com/maps?q=$lat,$lon"

        // Generate the static map image URL for display
        val mapImageUrl = "https://maps.googleapis.com/maps/api/staticmap?center=$lat,$lon&zoom=15&size=1000x1000&markers=color:red%7C$lat,$lon&key=${BuildConfig.MAPS_API_KEY}"

        // Load the map image into the ImageView
        Glide.with(this@SpecialistDetailActivity)
            .load(mapImageUrl)
            .error(R.drawable.image_placeholder)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.specialistMapsImageView)
    }

    private fun openGoogleMaps(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        // Try to open Google Maps in the app or browser
        try {
            val googleMapsUri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, googleMapsUri)
            intent.setPackage("com.google.android.apps.maps") // Opens Google Maps app if installed
            startActivity(intent)
        } catch (e: Exception) {
            // If Google Maps app is not installed, fallback to Custom Tabs (opens in browser)
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun redirectToWhatsApp(phoneNumber: String, message: String) {
        try {
            val uri = Uri.parse("https://wa.me/$phoneNumber?text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            // Handle exception if WhatsApp is not installed
            showToast(this, "WhatsApp is not installed on your device")
        }
    }

    companion object {
        const val EXTRA_SPECIALIST_ID = "EXTRA_SPECIALIST_ID"
    }

}