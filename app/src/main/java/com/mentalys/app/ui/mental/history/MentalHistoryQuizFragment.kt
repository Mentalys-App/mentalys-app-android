package com.mentalys.app.ui.mental.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.databinding.FragmentVoiceTestHistoryBinding
import com.mentalys.app.ui.mental.adapters.VoiceHistoryAdapter
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MentalHistoryQuizFragment : Fragment() {
class VoiceTestHistoryFragment : Fragment() {
    private var _binding: FragmentVoiceTestHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VoiceHistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: VoiceHistoryAdapter
    private lateinit var token: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mental_history_quiz, container, false)
        _binding = FragmentVoiceTestHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VoiceHistoryAdapter()

        lifecycleScope.launch {
            token = SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
            viewModel.getVoiceTestHistory(token)
        }

        observeLoadingState()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvVoiceHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@VoiceTestHistoryFragment.adapter
        }
    }

    private fun observeLoadingState() {
//        lifecycleScope.launch {
//            viewModel.loadingState.collectLatest { result ->
//                when (result) {
//                    is Result.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                    }
//
//                    is Result.Success -> {
//                        binding.progressBar.visibility = View.GONE
//                    }
//
//                    is Result.Error -> {
//                        binding.progressBar.visibility = View.GONE
//                        showToast(requireContext(), "Error: ${result.error}")
//                    }
//                }
//            }
//        }

        lifecycleScope.launch {
            viewModel.voice.observe(viewLifecycleOwner) { result ->
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

