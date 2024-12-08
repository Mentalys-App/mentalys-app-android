package com.mentalys.app.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentProfileBinding
import com.mentalys.app.ui.activities.SettingsActivity
import com.mentalys.app.ui.reachout.ReachOutBottomSheet
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var uid: String? = null
    private var token: String? = null
    private var fullName: String? = null
    private var username: String? = null

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

        // Load session data and fetch profile
        lifecycleScope.launch {
            getUserSessionData()
        }

        // Set up click listeners for profile actions
        binding.profileDetailLayout.setOnClickListener {
            val intent = Intent(activity, ProfileDetail::class.java)
            startActivity(intent)
        }

        binding.reachOutLayout.setOnClickListener {
            val bottomSheet = ReachOutBottomSheet { selectedEmotion ->
                openWhatsAppContact(selectedEmotion)
            }
            bottomSheet.show(requireActivity().supportFragmentManager, "StateBottomSheet")
        }

        binding.mindCareLayout.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:119")
            }
            startActivity(dialIntent)
        }

    }

    private fun openWhatsAppContact(state: String) {
        try {
            val message = getString(R.string.reach_out_message, state)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
                setPackage("com.whatsapp")
            }
            startActivity(sendIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            val appPackageName = "com.whatsapp"
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }
    }

    private suspend fun getUserSessionData() {
        uid = SettingsPreferences.getInstance(requireContext().dataStore).getUidSetting().first()
        token =
            SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
        fullName =
            SettingsPreferences.getInstance(requireContext().dataStore).getFullNameSetting().first()
        username =
            SettingsPreferences.getInstance(requireContext().dataStore).getUsernameSetting().first()

        // Update UI with user data
        binding.nameTextView.text = fullName
        binding.usernameTextView.text = "@$username"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}