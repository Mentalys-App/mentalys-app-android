package com.mentalys.app.ui.mental.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.databinding.FragmentMentalHistoryHandwritingBinding
import com.mentalys.app.ui.mental.adapters.HandwritingTestAdapter
import com.mentalys.app.databinding.FragmentHandwritingTestHistoryBinding
import com.mentalys.app.ui.mental.adapters.HandwritingHistoryAdapter
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.launch
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first


class MentalHistoryHandwritingFragment : Fragment() {
    private var _binding: FragmentMentalHistoryHandwritingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MentalHistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: HandwritingHistoryAdapter
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMentalHistoryHandwritingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HandwritingHistoryAdapter()

        lifecycleScope.launch {
            token = SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
            viewModel.getHandwritingHistory(token)
        }

        observeLoadingState()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvHandwritingHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MentalHistoryHandwritingFragment.adapter
        }
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.handwriting.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(result.data)
                    }

                    is Resource.Error -> {
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