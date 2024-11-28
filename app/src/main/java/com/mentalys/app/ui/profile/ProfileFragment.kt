package com.mentalys.app.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.databinding.FragmentProfileBinding
import com.mentalys.app.ui.activities.MainActivity
import com.mentalys.app.ui.activities.SettingsActivity
import com.mentalys.app.ui.auth.AuthViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var uid: String? = null
    private var token: String? = null
    private var email: String? = null

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

        getUserSessionData()

        viewLifecycleOwner.lifecycleScope.launch {
            binding.nameTextView.text =
                SettingsPreferences.getInstance(requireContext().dataStore).getEmailSetting()
                    .first()
        }

        binding.profileLayout.setOnClickListener {
            val intent = Intent(activity, ProfileDetail::class.java)
            startActivity(intent)
        }

    }

    private fun getUserSessionData() {
        lifecycleScope.launch {
            uid =
                SettingsPreferences.getInstance(requireContext().dataStore).getUidSetting().first()
            token = SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting()
                .first()
            email = SettingsPreferences.getInstance(requireContext().dataStore).getEmailSetting()
                .first()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}