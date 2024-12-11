package com.mentalys.app.ui.mental.history.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.FragmentMentalHistoryQuizBinding
import com.mentalys.app.ui.mental.history.MentalHistoryViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MentalHistoryQuizFragment : Fragment() {

    private var _binding: FragmentMentalHistoryQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MentalHistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: MentalHistoryQuizAdapter
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMentalHistoryQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MentalHistoryQuizAdapter()

        lifecycleScope.launch {
            token = SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting()
                .first()
            viewModel.getQuizHistory(token)
        }

        observeLoadingState()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvQuizHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MentalHistoryQuizFragment.adapter
        }
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.quiz.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (result.data.isEmpty()) {
                            binding.rvQuizHistory.visibility = View.GONE
                            binding.noDataFoundLottie.visibility = View.VISIBLE
                        } else {
                            binding.rvQuizHistory.visibility = View.VISIBLE
                            binding.noDataFoundLottie.visibility = View.GONE
                            adapter.submitList(result.data)
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvQuizHistory.visibility = View.GONE
                        binding.noDataFoundLottie.visibility = View.VISIBLE
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

