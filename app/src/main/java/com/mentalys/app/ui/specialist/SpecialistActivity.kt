package com.mentalys.app.ui.specialist

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivitySpecialistBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class SpecialistActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpecialistBinding

    private val viewModel: SpecialistViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var specialistAdapter: SpecialistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpecialistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener { finish() }

        specialistAdapter = SpecialistAdapter()

        // Trigger fetching of specialists
        viewModel.getSpecialists()

        // Observe specialists LiveData
        viewModel.specialists.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    specialistAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    specialistAdapter.setLoadingState(false)
                    specialistAdapter.submitList(resource.data)
                    Log.d("SpecialistActivity", resource.data.toString())
                }

                is Resource.Error -> {
                    showToast(this, resource.error)
                    Log.d("SpecialistActivity", "resource.data.toString()")
                }
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = specialistAdapter
        }

    }


}