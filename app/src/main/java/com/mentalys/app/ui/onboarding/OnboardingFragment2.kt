package com.mentalys.app.ui.onboarding

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mentalys.app.databinding.FragmentOnboarding2Binding
import com.mentalys.app.ui.activities.MainActivity

class OnboardingFragment2 : Fragment() {

    private var _binding: FragmentOnboarding2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboarding2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboardingSkip.setOnClickListener {
            // Save that onboarding is complete
            val prefs = activity?.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            if (prefs != null) {
                with(prefs.edit()) {
                    putBoolean("isFirstRun", false)
                    apply()
                }
            }

            // Redirect to MainActivity
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}