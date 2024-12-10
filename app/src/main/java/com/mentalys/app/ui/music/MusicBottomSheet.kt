package com.mentalys.app.ui.music

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mentalys.app.R
import com.mentalys.app.data.local.entity.MusicDetailEntity
import com.mentalys.app.data.remote.response.music.MusicDetailResponse
import com.mentalys.app.databinding.FragmentMentalHistoryBinding
import com.mentalys.app.databinding.LayoutBottomSheetMusicBinding
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.Resource

class MusicBottomSheet(private val id: Int) : BottomSheetDialogFragment() {

    private var mediaPlayer: MediaPlayer? = null
    private var _binding: LayoutBottomSheetMusicBinding? = null
    private val binding get() = _binding!!
    private lateinit var exoPlayer: ExoPlayer
    private val viewModel: MusicViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutBottomSheetMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exoPlayer = ExoPlayer.Builder(requireContext()).build()

        // Observe music data
        viewModel.getMusic(id) // Fetch music detail using ID
        viewModel.music.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = resource.data
                    binding.tvMusicTitle.text = data.name
                    binding.tvUsername.text = "Uploaded by: ${data.username}"

                    // Handle Play Preview
                    val mediaItem = data.previews?.previewHqMp3?.let { MediaItem.fromUri(it) }
                    if (mediaItem != null) {
                        exoPlayer.setMediaItem(mediaItem)
                    }
                    exoPlayer.prepare()

                    binding.btnPlayPreview.setOnClickListener {
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                            binding.btnPlayPreview.text = "Play Preview"
                        } else {
                            exoPlayer.play()
                            binding.btnPlayPreview.text = "Pause Preview"
                        }
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("MusicBottomSheet", resource.error)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        exoPlayer.release()
    }
}
