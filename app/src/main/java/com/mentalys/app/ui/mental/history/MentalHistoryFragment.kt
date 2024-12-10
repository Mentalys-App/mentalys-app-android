package com.mentalys.app.ui.mental.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.mentalys.app.databinding.FragmentMentalHistoryBinding
import com.mentalys.app.ui.auth.AuthLoginActivity
import com.mentalys.app.ui.auth.AuthRegisterActivity
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MentalHistoryFragment : Fragment() {

    private var _binding: FragmentMentalHistoryBinding? = null
    private val binding get() = _binding!!
//    private lateinit var mentalHistoryAdapter: MentalHistoryAdapter
    private var isLogin: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMentalHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            isLogin = SettingsPreferences.getInstance(requireContext().dataStore).getIsLoginSetting().first()
            if (isLogin == true) {
                binding.historyLoggedIn.visibility = View.VISIBLE
                binding.historyLoggedOut.visibility = View.INVISIBLE
                setupAdapter()
            } else {
                binding.historyLoggedIn.visibility = View.GONE
                binding.historyLoggedOut.visibility = View.VISIBLE
                binding.loginButton.setOnClickListener {
                    startActivity(Intent(requireActivity(), AuthLoginActivity::class.java))
                }
                binding.registerButton.setOnClickListener {
                    startActivity(Intent(requireActivity(), AuthRegisterActivity::class.java))
                }
            }
        }

    }

    private fun setupAdapter() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        val adapter = MentalHistoryViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Voice"
                1 -> tab.text = "Handwriting"
                2 -> tab.text = "Quiz"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}