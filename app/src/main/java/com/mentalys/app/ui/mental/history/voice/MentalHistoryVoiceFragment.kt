package com.mentalys.app.ui.mental.history.voice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.databinding.FragmentMentalHistoryVoiceBinding
import com.mentalys.app.ui.mental.history.MentalHistoryViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MentalHistoryVoiceFragment : Fragment() {

    private var _binding: FragmentMentalHistoryVoiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MentalHistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: MentalHistoryVoiceAdapter
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMentalHistoryVoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MentalHistoryVoiceAdapter()

        lifecycleScope.launch {
            token = SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
            viewModel.getVoiceHistory(token)
        }

        observeLoadingState()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvVoiceHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MentalHistoryVoiceFragment.adapter
        }
    }

    private fun observeLoadingState() {
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