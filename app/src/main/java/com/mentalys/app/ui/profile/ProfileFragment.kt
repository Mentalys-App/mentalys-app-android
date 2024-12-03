package com.mentalys.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.databinding.FragmentProfileBinding
import com.mentalys.app.ui.activities.SettingsActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var uid: String? = null
    private var token: String? = null
    private var email: String? = null
    private var isNewProfile: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsImageView.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Initially hide both layouts and show loading
        binding.profileAddLayout.visibility = View.GONE
        binding.profileDetailLayout.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        // Load session data and fetch profile
        lifecycleScope.launch {
            getUserSessionData()
            checkUserProfile()
        }

        // Set up click listeners for profile actions
        binding.profileDetailLayout.setOnClickListener {
            showToast(requireContext(), "edit profile clicked")
            val intent = Intent(activity, ProfileDetail::class.java)
            intent.putExtra("IS_NEW_PROFILE", false)
            startActivity(intent)
        }

        binding.profileAddLayout.setOnClickListener {
            showToast(requireContext(), "add profile clicked")
            val intent = Intent(activity, ProfileDetail::class.java)
            intent.putExtra("IS_NEW_PROFILE", true)
            startActivity(intent)
        }

    }

    private suspend fun getUserSessionData() {
        uid = SettingsPreferences.getInstance(requireContext().dataStore).getUidSetting().first()
        token =
            SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
        email =
            SettingsPreferences.getInstance(requireContext().dataStore).getEmailSetting().first()

        // Update UI with user data
        binding.nameTextView.text = email
        showToast(requireContext(), "email: $email token: $token")
        Log.d("TOKENNN", token.toString())
    }

    private suspend fun checkUserProfile() {
        // Get isHasProfile value from shared preferences
        val isHasProfile =
            SettingsPreferences.getInstance(requireContext().dataStore).getIsHaveProfileSetting()
                .first()

        // Set initial state based on isHasProfile
        if (isHasProfile) {
            showProfileDetails()
//            navigateToProfileDetail(isNewProfile = false)
            isNewProfile = false
        } else {
//            showAddProfile()


// todo: check state when is not login -> i think i don't have to because i assume user must be online after login
            // Attempt to fetch profile from API
            token?.let {
                viewModel.getProfile(it).observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Loading -> showLoading()
                        is Resource.Success -> {
                            // Profile found; update UI and save state to preferences
                            showProfileDetails()
                            saveProfileStatusToPreferences(true)
                            isNewProfile = false

                            // Assuming `resource.data.data` contains the profile information
                            resource.data.data?.let { profile ->
                                if (
                                    profile.fullName != null &&
                                    profile.birthDate != null &&
                                    profile.username != null &&
                                    profile.gender != null &&
                                    profile.location != null &&
                                    profile.profilePic != null &&
                                    profile.uid != null &&
                                    profile.createdAt != null &&
                                    profile.updatedAt != null
                                ) {
                                    // Launch a coroutine to ensure sequential execution
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        // Save the profile session
                                        viewModel.saveProfileSession(
                                            fullName = profile.fullName,
                                            birthDate = profile.birthDate,
                                            username = profile.username,
                                            gender = profile.gender,
                                            location = profile.location,
                                            profilePic = profile.profilePic,
                                            uid = profile.uid,
                                            createdAt = profile.createdAt,
                                            updatedAt = profile.updatedAt,
                                        )
                                    }
                                    // Log all values
                                    Log.d(
                                        "ProfileDetail",
                                        """
                                            Context: ${requireContext()}
                                            Updated At: ${profile.updatedAt}
                                            Created At: ${profile.createdAt}
                                            Birth Date: ${profile.birthDate}
                                            Gender: ${profile.gender}
                                            Username: ${profile.username}
                                            Profile Pic: ${profile.profilePic}
                                            Location: ${profile.location}
                                            """.trimIndent()
                                    )
                                } else {
                                    Log.e(
                                        "ProfileDetail",
                                        "Missing profile fields, unable to save session."
                                    )
                                    Log.d(
                                        "ProfileDetail",
                                        """
                                            Context: ${requireContext()}
                                            Updated At: ${profile.updatedAt}
                                            Created At: ${profile.createdAt}
                                            Birth Date: ${profile.birthDate}
                                            Gender: ${profile.gender}
                                            Username: ${profile.username}
                                            Profile Pic: ${profile.profilePic}
                                            Location: ${profile.location}
                                            """.trimIndent()
                                    )
                                }
                            } ?: run {
                                Log.e("ProfileDetail", "Profile data is null.")
                            }

                        }

                        is Resource.Error -> {
                            // If the API fails, fall back to the shared preference value
                            if (isHasProfile) showProfileDetails()
                            else showAddProfile()
                            isNewProfile = true
                        }
                    }
                }
            }


//            token?.let {
//                viewModel.getProfile(it).observe(viewLifecycleOwner) { resource ->
//                    when (resource) {
//                        is Resource.Success -> {
//                            // Profile exists in server, save locally
//                            lifecycleScope.launch {
//                                SettingsPreferences.getInstance(requireContext().dataStore)
//                                    .saveIsHaveProfileSetting(true)
//                            }
//                            navigateToProfileDetail(isNewProfile = false)
//                        }
//                        is Resource.Error -> {
//                            // Profile not found, start with empty profile
//                            navigateToProfileDetail(isNewProfile = true)
//                        }
//                        else -> Unit
//                    }
//                }
//            }
        }


    }

//    private fun navigateToProfileDetail(isNewProfile: Boolean) {
//        val intent = Intent(requireContext(), ProfileDetail::class.java).apply {
//            putExtra("IS_NEW_PROFILE", isNewProfile)
//        }
//        startActivity(intent)
//    }

    // Helper to update preferences
    private fun saveProfileStatusToPreferences(hasProfile: Boolean) {
        lifecycleScope.launch {
            SettingsPreferences.getInstance(requireContext().dataStore)
                .saveIsHaveProfileSetting(hasProfile)
        }
    }

    // Helper methods for showing UI states
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.profileAddLayout.visibility = View.GONE
        binding.profileDetailLayout.visibility = View.GONE
    }

    private fun showProfileDetails() {
        binding.progressBar.visibility = View.GONE
        binding.profileAddLayout.visibility = View.GONE
        binding.profileDetailLayout.visibility = View.VISIBLE
    }

    private fun showAddProfile() {
        binding.progressBar.visibility = View.GONE
        binding.profileDetailLayout.visibility = View.GONE
        binding.profileAddLayout.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}