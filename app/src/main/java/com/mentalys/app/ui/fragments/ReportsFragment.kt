package com.mentalys.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.data.remote.response.mental_test.HistoryItem
import com.mentalys.app.databinding.FragmentReportsBinding
import com.mentalys.app.ui.activities.TestGemini
import com.mentalys.app.ui.adapters.HistoryAdapter
import com.mentalys.app.ui.onboarding.OnboardingActivity
import com.mentalys.app.ui.viewmodels.ReportsViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Result
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReportsFragment : Fragment() {
    private val reportsViewModel: ReportsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyAdapter = HistoryAdapter()
        setupRecyclerView()
//        binding.generate.setOnClickListener {
//            startActivity(Intent(activity, TestGemini::class.java))
//        }
//
//        binding.onboarding.setOnClickListener {
//            startActivity(Intent(activity, OnboardingActivity::class.java))
//        }
        fetchHistoryData()
        observeData()

    }

    private fun fetchHistoryData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val token =
                SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
            reportsViewModel.fetchHistory(
                token = token,
                page = 1,
                limit = 10,
                startDate = "2024-11-15T10:30:00Z",
                endDate = "2024-12-15T23:59:59Z",
                sortBy = "timestamp",
                sortOrder = "desc"
            )
        }
    }

    private fun observeData() {
        reportsViewModel.historyResult.observe(viewLifecycleOwner) { history ->
            handleResult(history)
        }
    }

    private fun handleResult(result: Result<List<HistoryItem>>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is Result.Success -> {
                binding.progressBar.visibility = View.GONE
                historyAdapter.submitList(result.data)
            }

            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Terjadi kesalahan: ${result.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (result is Result.Success && result.data.isEmpty()) {
            Toast.makeText(context, "Event tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onResume() {
        super.onResume()
        fetchHistoryData()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReportsFragment.historyAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}