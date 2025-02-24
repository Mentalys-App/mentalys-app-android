package com.mentalys.app.ui.mental.test.handwriting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMentalTestHandwritingBinding
import com.mentalys.app.ui.activities.CameraActivity
import com.mentalys.app.ui.activities.CameraActivity.Companion.CAMERAX_RESULT
import com.mentalys.app.ui.mental.test.MentalTestResultActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.reduceFileImage
import com.mentalys.app.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File


class MentalTestHandwritingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMentalTestHandwritingBinding
    private var currentImageUri: Uri? = null
    private val viewModel: MentalTestHandwritingViewModel by viewModels {
        ViewModelFactory.getInstance(
            this@MentalTestHandwritingActivity
        )
    }
    private var isBackButtonDisabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalTestHandwritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.handwritingImgPreview.setOnClickListener { startCamera() }
        binding.analyseButton.setOnClickListener {
            lifecycleScope.launch {
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
            launchUCrop(uri)
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
                        launchUCrop(uri)
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
            showToast(this, getString(R.string.alert_empty_image))
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
        } ?: showToast(this, getString(R.string.alert_empty_image))
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
                    val confidence = response.prediction?.confidencePercentage // response: 11.0%
                    val imageUri = viewModel.currentImageUri.value

                    if (prediction != null && confidence != null) {
                        moveToResult(prediction, confidence, imageUri)
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showToast(this, result.error)
                }
            }
        }
    }

    private fun moveToResult(label: String, confidence: String, imageUri: Uri?) {
        val intent = Intent(this, MentalTestResultActivity::class.java).apply {
            putExtra(MentalTestResultActivity.EXTRA_TEST_NAME, "handwriting")
            putExtra(MentalTestResultActivity.EXTRA_PREDICTION, label)
            putExtra(MentalTestResultActivity.EXTRA_CONFIDENCE_PERCENTAGE, confidence)
            putExtra(MentalTestResultActivity.EXTRA_IMAGE_URI, imageUri?.toString())
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        val loadingScreen = findViewById<View>(R.id.handwriting_test_loading_layout)
        loadingScreen.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.handwritingTestLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        isBackButtonDisabled = isLoading
    }

    private val launcherUCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                viewModel.setImageUri(uri)
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            showToast(this, "Crop error: ${cropError?.message}")
        }
    }

    private fun launchUCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(this.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
        )
        val uCropIntent = UCrop.of(sourceUri, destinationUri)
            .getIntent(this)
        launcherUCrop.launch(uCropIntent)
    }

    override fun onBackPressed() {
        if (isBackButtonDisabled) {
            showToast(this@MentalTestHandwritingActivity,  getString(R.string.be_patient_please_wait))
            return
        }
        super.onBackPressed()
    }


}