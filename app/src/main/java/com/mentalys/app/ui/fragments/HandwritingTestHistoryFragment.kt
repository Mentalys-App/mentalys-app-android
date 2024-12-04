package com.mentalys.app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.databinding.FragmentHandwritingTestHistoryBinding
import com.mentalys.app.ui.adapters.HandwritingTestAdapter
import com.mentalys.app.ui.viewmodels.HandwritingTestHistoryViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.mentalys.app.utils.Result
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first


class HandwritingTestHistoryFragment : Fragment() {
    private var _binding: FragmentHandwritingTestHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HandwritingTestHistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: HandwritingTestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHandwritingTestHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HandwritingTestAdapter()
        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.getHandwritingHistory(
                SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting()
                    .first()
            ).collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        observeLoadingState()
    }

    private fun setupRecyclerView() {
        binding.rvHandwritingHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HandwritingTestHistoryFragment.adapter
        }
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.loadingState.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(requireContext(), "Error: ${result.error}")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}