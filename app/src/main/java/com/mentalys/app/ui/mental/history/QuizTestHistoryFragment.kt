package com.mentalys.app.ui.mental.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.databinding.FragmentQuizTestHistoryBinding
import com.mentalys.app.ui.mental.adapters.HandwritingHistoryAdapter
import com.mentalys.app.ui.mental.adapters.QuizAdapter
import com.mentalys.app.ui.mental.adapters.QuizHistoryAdapter
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import com.mentalys.app.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizTestHistoryFragment : Fragment() {
    private var _binding: FragmentQuizTestHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizHistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: QuizHistoryAdapter
    private lateinit var token: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizTestHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = QuizHistoryAdapter()

        lifecycleScope.launch {
            token = SettingsPreferences.getInstance(requireContext().dataStore).getTokenSetting().first()
            viewModel.getQuizTestHistory(token)
        }

        observeLoadingState()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvQuizHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@QuizTestHistoryFragment.adapter
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
            viewModel.quiz.observe(viewLifecycleOwner) { result ->
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