package com.mentalys.app.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityHandwritingTestBinding
import com.mentalys.app.ui.activities.CameraActivity.Companion.CAMERAX_RESULT
import com.mentalys.app.ui.viewmodels.HandwritingTestViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory

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

        viewModel.currentImageUri.observe(this) { uri ->
            currentImageUri = uri
            if (uri != null) {
                showImage()
            }
        }
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
            result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()?.let { uri ->
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

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA

    }
}