package com.mentalys.app.ui.mental.test.handwriting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityHandwritingTestBinding
import com.mentalys.app.ui.activities.CameraActivity
import com.mentalys.app.ui.activities.CameraActivity.Companion.CAMERAX_RESULT
import com.mentalys.app.ui.mental.MentalTestResultActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.reduceFileImage
import com.mentalys.app.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HandwritingTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHandwritingTestBinding
    private var currentImageUri: Uri? = null
    private val viewModel: HandwritingTestViewModel by viewModels {
        ViewModelFactory.getInstance(
            this@HandwritingTestActivity
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, R.string.permision_request_granted, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.permision_request_denied, Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,

            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHandwritingTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.handwritingImgPreview.setOnClickListener { startCamera() }
        binding.analyseButton.setOnClickListener {
            lifecycleScope.launch {
                analyseImage(SettingsPreferences.getInstance(dataStore).getTokenSetting().first())
                analyseImage(SettingsPreferences.getInstance(dataStore).getTokenSetting().first())
            }
        }
        viewModel.currentImageUri.observe(this) { uri ->
            currentImageUri = uri
            if (uri != null) {
                showImage()
            }
        }
        setupObservers()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            viewModel.setImageUri(currentImageUri!!)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == CAMERAX_RESULT) {
                result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
                    ?.let { uri ->
                        viewModel.setImageUri(uri)
                    }
            }
        }

    private fun showImage() {
        binding.handwritingImgIcon.visibility = View.GONE
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.handwritingImgPreview.setImageURI(it)
        }
    }

    private fun analyseImage(token: String) {
        if (currentImageUri == null) {
            showToast(getString(R.string.alert_empty_image))
            return
        }

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )
            viewModel.handwritingTest(token, multipartBody)
        } ?: showToast(getString(R.string.alert_empty_image))
    }

    private fun setupObservers() {
        viewModel.testResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(true)
                    val response = result.data
                    val prediction = response.prediction?.result
                    val confidence = response.prediction?.confidencePercentage
                    val imageUri = viewModel.currentImageUri.value

                    if (prediction != null && confidence != null) {
                        moveToResult(prediction, confidence, imageUri)
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun moveToResult(label: String, confidence: String, imageUri: Uri?) {
        val intent = Intent(this, MentalTestResultActivity::class.java).apply {
            putExtra(MentalTestResultActivity.EXTRA_PREDICTION, label)
            putExtra(MentalTestResultActivity.EXTRA_CONFIDENCE_PERCENTAGE, confidence)
            putExtra(MentalTestResultActivity.EXTRA_IMAGE_URI, imageUri?.toString())
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        val loadingScreen = findViewById<View>(R.id.loadingLayout)
        loadingScreen.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.layoutHandwritingTest.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this@HandwritingTestActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA

    }
}