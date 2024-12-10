package com.mentalys.app.ui.music

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMusicBinding
import com.mentalys.app.ui.specialist.SpecialistAdapter
import com.mentalys.app.ui.specialist.SpecialistViewModel
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.showToast

class MusicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicBinding
    private lateinit var musicAdapter: MusicAdapter
    private val viewModel: MusicViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener { finish() }

        musicAdapter = MusicAdapter(fragmentManager = supportFragmentManager)
        viewModel.getMusics("peaceful piano")

        // Observe specialists LiveData
        viewModel.musics.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    musicAdapter.setLoadingState(true)
                }

                is Resource.Success -> {
                    musicAdapter.setLoadingState(false)
                    musicAdapter.submitList(resource.data)
                    Log.d("MusicActivity", resource.data.toString())
                }

                is Resource.Error -> {
                    Log.d("MusicActivity", resource.error)
                }
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = musicAdapter
        }

    }

}