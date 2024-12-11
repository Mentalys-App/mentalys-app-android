package com.mentalys.app.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityProfileDetailBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.auth.AuthViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.reduceFileImage
import com.mentalys.app.utils.showToast
import com.mentalys.app.utils.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileDetail : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDetailBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this@ProfileDetail)
    }
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this@ProfileDetail)
    }
    private lateinit var status: String
    private var currentImageUri: Uri? = null
    private var isEditMode = false
    private lateinit var token: String

    private var previousImageUri: Uri? = null
    private var previousFullName: String = ""
    private var previousUsername: String = ""
    private var previousDob: String = ""
    private var previousGender: String = ""
    private var previousLocation: String = ""

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            preloadProfileData()
        }

        setupListeners()

        // Observe changes in the ViewModel for the image URI
//        viewModel.currentImageUri.observe(this) { uri ->
//            currentImageUri = uri
//            if (uri != null) {
//                binding.profileImageView.setImageURI(uri)
//            } else {
//                // Use default icon if URI is null
//                binding.profileImageView.setImageResource(R.drawable.ic_account_circle)
//            }
//        }

    }

    private fun setupForNewProfile() {
        isEditMode = true
        toggleEditMode() // Enable editing mode

        // Clear all fields
        binding.profileFullNameEditText.text = null
        binding.profileUsernameEditText.text = null
        binding.profileDobEditText.text = null
        binding.profileGenderEditText.text = null
        binding.profileLocationEditText.text = null

        // Use default icon for profile picture
        binding.profileImageView.setImageResource(R.drawable.ic_account_circle)

        // Hide "Save Changes" and show "Create Profile"
        binding.profileEditTextView.text = getString(R.string.create_profile)

        // Remove save confirmation dialog and directly save the profile
        binding.profileEditTextView.setOnClickListener {
//            saveNewProfile()
            showToast(this@ProfileDetail, "todo: create new profile")
        }
    }

    private suspend fun preloadProfileData() {
        val preferences = SettingsPreferences.getInstance(dataStore)
        binding.profileFullNameEditText.setText(preferences.getFullNameSetting().first())
        binding.profileUsernameEditText.setText(preferences.getUsernameSetting().first())
        binding.profileEmailTextView.text = preferences.getEmailSetting().first()
        binding.profilePhoneNumberEditText.setText(preferences.getPhoneNumberSetting().first())
//        binding.profileDobEditText.setText(preferences.getBirthDateSetting().first())
//        binding.profileGenderEditText.setText(preferences.getGenderSetting().first())
//        binding.profileLocationEditText.setText(preferences.getLocationSetting().first())

        // Use existing profile picture if available
//        val profilePicUri = preferences.getProfilePicSetting().first()
//        if (profilePicUri.isNotEmpty()) {
//            binding.profileImageView.setImageURI(Uri.parse(profilePicUri))
//        } else {
//            binding.profileImageView.setImageResource(R.drawable.ic_account_circle)
//        }

        // Disable editing by default
//        toggleEditMode()
    }

    private fun saveNewProfile() {
        val fullName = binding.profileFullNameEditText.text.toString()
        val username = binding.profileUsernameEditText.text.toString()
        val dob = binding.profileDobEditText.text.toString()
        val gender = binding.profileGenderEditText.text.toString()
        val location = binding.profileLocationEditText.text.toString()

        if (fullName.isEmpty() || username.isEmpty()) {
            showToast(this, "Full name and username are required.")
            return
        }

        val profilePicFile = currentImageUri?.let { uriToFile(it, this).reduceFileImage() }

        profileViewModel.updateProfile(
            token = token,
            username = username,
            fullName = fullName,
            birthDate = dob,
            gender = gender,
            location = location,
            profilePicFile = profilePicFile
        ).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(this, "Profile created successfully!")
                    finish() // Return to the previous screen
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(this, "Failed to create profile: ${resource.error}")
                }
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setOnClickListener { finish() }
            logoutLayout.setOnClickListener { showLogoutConfirmationDialog() }
//            profileImageView.setOnClickListener {
//                if (isEditMode) launcherGallery.launch(
//                    PickVisualMediaRequest(
//                        ActivityResultContracts.PickVisualMedia.ImageOnly
//                    )
//                )
//                else showToast(this@ProfileDetail, "You must enter edit mode to change the image")
//            }
//            profileEditTextView.setOnClickListener {
//                if (isEditMode) showSaveConfirmationDialog()
//                else toggleEditMode()
//            }
        }
    }

    private fun toggleEditMode() {
        isEditMode = !isEditMode // Toggle the edit mode state

        binding.profileEditTextView.text = if (isEditMode) "Save Changes" else "Edit Profile"

        // Enable or disable fields and actions based on edit mode
        binding.profileImageView.isEnabled = isEditMode
        binding.profileFullNameEditText.isEnabled = isEditMode
        binding.profileUsernameEditText.isEnabled = isEditMode
        binding.profileDobEditText.isEnabled = isEditMode
        binding.profileGenderEditText.isEnabled = isEditMode
        binding.profileLocationEditText.isEnabled = isEditMode

//        if (!isEditMode) {
//            // Show confirmation dialog before saving changes
//            saveChanges()
//        }

        if (isEditMode) {
            // Store current values as previous values when entering edit mode
            previousImageUri = currentImageUri // Save the current image URI
            previousFullName = binding.profileFullNameEditText.text.toString()
            previousUsername = binding.profileUsernameEditText.text.toString()
            previousDob = binding.profileDobEditText.text.toString()
            previousGender = binding.profileGenderEditText.text.toString()
            previousLocation = binding.profileLocationEditText.text.toString()
        }

    }

    private fun discardChanges() {
        // Reset fields to previous values
        binding.profileFullNameEditText.setText(previousFullName)
        binding.profileUsernameEditText.setText(previousUsername)
        binding.profileDobEditText.setText(previousDob)
        binding.profileGenderEditText.setText(previousGender)
        binding.profileLocationEditText.setText(previousLocation)

        // Restore the previous image URI
        currentImageUri = previousImageUri
        if (previousImageUri != null) {
            binding.profileImageView.setImageURI(previousImageUri)
        } else {
            binding.profileImageView.setImageResource(R.drawable.ic_account_circle)
        }

        // Show a toast to indicate changes were discarded
        showToast(this, "Changes discarded")
    }

    private fun saveChanges() {
        // Get the current values from the fields
        val currentFullName = binding.profileFullNameEditText.text.toString()
        val currentUsername = binding.profileUsernameEditText.text.toString()
        val currentDob = binding.profileDobEditText.text.toString()
        val currentGender = binding.profileGenderEditText.text.toString()
        val currentLocation = binding.profileLocationEditText.text.toString()

        // Check for changes and prepare the request bodies for modified fields
        val modifiedFields = mutableListOf<Pair<String, String?>>()

        // Only add fields to the request if they've changed
        if (currentFullName != previousFullName) {
            modifiedFields.add("full_name" to currentFullName)
        }
        if (currentUsername != previousUsername) {
            modifiedFields.add("username" to currentUsername)
        }
        if (currentDob != previousDob) {
            modifiedFields.add("birth_date" to currentDob)
        }
        if (currentGender != previousGender) {
            modifiedFields.add("gender" to currentGender)
        }
        if (currentLocation != previousLocation) {
            modifiedFields.add("location" to currentLocation)
        }

        // If no fields are modified, show a message and return
        if (modifiedFields.isEmpty() && currentImageUri == previousImageUri) {
            showToast(this, "No changes made.")
            return
        }

//        // Prepare the profile picture file if it has changed
//        val profilePicFile = if (currentImageUri != previousImageUri) {
//            currentImageUri?.let {
//                val file = File(getRealPathFromURI(it)) // Convert URI to file
//                file
//            }
//        } else {
//            null
//        }
//
//        // Log file path and check if profile picture file is correctly retrieved
//        Log.d("ProfileDetail", "Profile Picture File: ${profilePicFile?.absolutePath}")

        // Collect modified fields as a Map of Strings
        val requestParams = mutableMapOf<String, String?>()
        for ((key, value) in modifiedFields) {
            requestParams[key] = value
        }

        // Save changes logic (e.g., update ViewModel, persist data, etc.)
        profileViewModel.updateProfile(
            token = token,
            username = requestParams["username"],
            fullName = requestParams["full_name"],
            birthDate = requestParams["birth_date"],
            gender = requestParams["gender"],
            location = requestParams["location"],
            profilePicFile = currentImageUri?.let {
                uriToFile(
                    it,
                    this@ProfileDetail
                ).reduceFileImage()
            } // Only pass the profile picture file if it changed
        ).observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Log.d("ProfileDetail", "Updating profile: Loading...")
                    // Show loading indicator if needed
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    Log.d("ProfileDetail", "Profile updated successfully!")
                    showToast(this, "Changes saved")
                    binding.progressBar.visibility = View.GONE

                    // Save updated profile details to preferences
                    resource.data.data?.let { profile ->
                        if (
                            profile.updatedAt != null &&
                            profile.createdAt != null &&
                            profile.birthDate != null &&
                            profile.gender != null &&
                            profile.username != null &&
                            profile.profilePic != null &&
                            profile.location != null
                        ) {
//                            profileViewModel.saveProfileSession(
//                                birthDate = profile.birthDate,
//                                createdAt = profile.createdAt,
//                                updatedAt = profile.updatedAt,
//                                gender = profile.gender,
//                                username = profile.username,
//                                profilePic = profile.profilePic,
//                                location = profile.location,
//                            )
                        } else {
                            Log.e(
                                "ProfileDetail",
                                "Missing profile fields, unable to save session."
                            )
                        }
                    } ?: run {
                        Log.e("ProfileDetail", "Profile data is null.")
                    }

                    toggleEditMode() // Exit edit mode
                }

                is Resource.Error -> {
                    Log.d("ProfileDetail", "Error updating profile: ${resource.error}")
                    showToast(this, "Failed to save changes: ${resource.error}")
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun getRealPathFromURI(contentUri: Uri): String {
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex("_data") ?: -1
        val path = cursor?.getString(index)
        cursor?.close()
        return path ?: ""
    }


    private fun showSaveConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Changes")
            .setMessage("Do you want to save the changes?")
            .setPositiveButton("Yes") { _, _ ->
                // Simulate API call by showing a toast
                saveChanges()
                toggleEditMode() // Exit edit mode
            }
            .setNegativeButton("No") { _, _ ->
                // Discard changes and exit edit mode
                discardChanges()
                toggleEditMode() // Exit edit mode
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss() // Close the dialog, stay in edit mode
            }
            .show()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteLoginSession()
                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isEditMode", isEditMode)
        outState.putString("fullName", binding.profileFullNameEditText.text.toString())
        outState.putString("username", binding.profileUsernameEditText.text.toString())
        outState.putString("dob", binding.profileDobEditText.text.toString())
        outState.putString("gender", binding.profileGenderEditText.text.toString())
        outState.putString("location", binding.profileLocationEditText.text.toString())
        currentImageUri?.let { outState.putString("currentImageUri", it.toString()) }
        previousImageUri?.let { outState.putString("previousImageUri", it.toString()) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isEditMode = savedInstanceState.getBoolean("isEditMode", false)
        binding.profileEditTextView.text = if (isEditMode) "Save Changes" else "Edit Profile"

        // Restore EditText fields
        binding.profileFullNameEditText.setText(savedInstanceState.getString("fullName", ""))
        binding.profileUsernameEditText.setText(savedInstanceState.getString("username", ""))
        binding.profileDobEditText.setText(savedInstanceState.getString("dob", ""))
        binding.profileGenderEditText.setText(savedInstanceState.getString("gender", ""))
        binding.profileLocationEditText.setText(savedInstanceState.getString("location", ""))

        // Restore image URIs
        savedInstanceState.getString("currentImageUri")?.let {
            currentImageUri = Uri.parse(it)
            binding.profileImageView.setImageURI(currentImageUri)
        } ?: run {
            binding.profileImageView.setImageResource(R.drawable.ic_account_circle)
        }

        savedInstanceState.getString("previousImageUri")?.let {
            previousImageUri = Uri.parse(it)
        }

        // Enable or disable fields based on `isEditMode`
        binding.profileImageView.isEnabled = isEditMode
        binding.profileFullNameEditText.isEnabled = isEditMode
        binding.profileUsernameEditText.isEnabled = isEditMode
        binding.profileDobEditText.isEnabled = isEditMode
        binding.profileGenderEditText.isEnabled = isEditMode
        binding.profileLocationEditText.isEnabled = isEditMode
    }


}