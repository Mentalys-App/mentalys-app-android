package com.mentalys.app.ui.mental.test.voice


import AudioRecorder
import android.Manifest
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityMentalTestVoiceBinding
import com.mentalys.app.ui.mental.MentalTestResultActivity
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.utils.AudioUtils
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MentalTestVoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMentalTestVoiceBinding
    private val viewModel: MentalTestVoiceViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var audioRecorder: AudioRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFile: File? = null
    private var audioUri: Uri? = null

    private var isRecording = false
    private var isPlaying = false
    private var recordingDuration = 0L
    private var recordingTimer: CountDownTimer? = null

    // Recording time constraints
    private val minRecordingDuration = 15000L // 15 seconds in milliseconds
    private val maxRecordingDuration = 30000L // 30 seconds in milliseconds
    private var recordingStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalTestVoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()
        observeViewModel()
        setupListeners()
        updateUI()
        setupObservers()

        // Initialize timer text
        resetTimerDisplay()
    }

    private fun resetTimerDisplay() {
        binding.chronometer.text = "00:30"
    }

    private fun requestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(Manifest.permission.RECORD_AUDIO)
        } else {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsGranted ->
            if (!permissionsGranted.all { it.value }) {
                Toast.makeText(this, "Izin diperlukan untuk melanjutkan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        requestPermissionLauncher.launch(permissions)
    }

    private fun observeViewModel() {
        viewModel.audioFileUri.observe(this) { uri ->
            audioUri = uri
            audioFile = uri?.path?.let { File(it) }
            updateUI()
        }

        viewModel.isRecording.observe(this) { isRecording ->
            this.isRecording = isRecording
            updateUI()
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            this.isPlaying = isPlaying
            updateUI()
        }
    }

    private fun setupListeners() {
        binding.buttonRecordAudio.setOnClickListener {
            when {
                isRecording -> stopRecording()
                isPlaying -> stopPlayback()
                audioFile != null || audioUri != null -> playAudio()
                else -> startRecording()
            }
        }
        binding.buttonResetAudio.setOnClickListener { resetAudio() }
        binding.buttonSendAudio.setOnClickListener {
            lifecycleScope.launch {
                sendAudioToAPI(SettingsPreferences.getInstance(dataStore).getTokenSetting().first())
            }
        }
    }

    private fun updateUI() {
        binding.buttonResetAudio.isEnabled = audioFile != null || audioUri != null
        binding.buttonSendAudio.isEnabled = audioFile != null || audioUri != null

        binding.buttonRecordAudio.setImageResource(
            when {
                isRecording -> R.drawable.ic_stop
                isPlaying -> R.drawable.ic_stop
                audioFile != null || audioUri != null -> R.drawable.ic_play
                else -> R.drawable.ic_outline_voice
            }
        )
    }

    private fun startRecording() {
        val timestamp = System.currentTimeMillis()
        audioFile = AudioUtils.createAudioFile(this, timestamp) ?: return

        try {
            audioRecorder = AudioRecorder(this, audioFile!!.absolutePath)
            audioRecorder?.startRecording()

            // Start recording timer
            startRecordingTimer()

            recordingStartTime = SystemClock.elapsedRealtime()
            viewModel.setAudioFileUri(Uri.fromFile(audioFile))
            viewModel.setRecordingStatus(true)
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error starting recording: ${e.message}", e)
            showToast("Gagal memulai rekaman: ${e.message}")
        }
    }

    private fun startRecordingTimer() {
        // Cancel any existing timer
        recordingTimer?.cancel()

        // Create a new countdown timer for 30 seconds
        recordingTimer = object : CountDownTimer(maxRecordingDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.chronometer.text = String.format("00:%02d", secondsRemaining)
            }

            override fun onFinish() {
                // Automatically stop recording when timer reaches zero
                stopRecording()
            }
        }.start()
    }

    private fun stopRecording() {
        try {
            val currentRecordingDuration = SystemClock.elapsedRealtime() - recordingStartTime

            // Debug logging
            Log.d("VoiceTest", "Current recording duration: $currentRecordingDuration ms")

            // Prevent stopping before minimum duration
            if (currentRecordingDuration < minRecordingDuration) {
                showToast("Rekaman minimal 15 detik")
                return
            }

            audioRecorder?.stopRecording()
            audioRecorder = null

            // Cancel the recording timer
            recordingTimer?.cancel()

            // Store the actual recording duration
            recordingDuration = currentRecordingDuration

            // Update timer display with actual recording duration
            val seconds = currentRecordingDuration / 1000
            binding.chronometer.text = String.format("00:%02d", seconds)

            viewModel.setRecordingStatus(false)
            showToast("Rekaman selesai")
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error stopping recording: ${e.message}", e)
            showToast("Gagal menghentikan rekaman: ${e.message}")
        }
    }

    private fun playAudio() {
        try {
            if (audioFile == null || !audioFile!!.exists()) {
                Log.e("VoiceTest", "Audio file does not exist")
                showToast("File audio tidak ditemukan")
                return
            }

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFile!!.absolutePath)
                prepare()
                setOnErrorListener { mp, what, extra ->
                    Log.e("VoiceTest", "MediaPlayer error: what=$what, extra=$extra")
                    stopPlayback()
                    true
                }
                start()
                setOnCompletionListener { stopPlayback() }
            }

            startPlaybackTimer()
            viewModel.setPlayingStatus(true)
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error playing audio: ${e.message}", e)
            showToast("Gagal memutar audio: ${e.message}")
        }
    }

    private fun startPlaybackTimer() {
        // Use the actual recording duration for the timer
        recordingTimer = object : CountDownTimer(recordingDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.chronometer.text = String.format("00:%02d", secondsRemaining)
            }

            override fun onFinish() {
                stopPlayback()
            }
        }.start()
    }

    private fun stopPlayback() {
        try {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null

            recordingTimer?.cancel()

            val seconds = recordingDuration / 1000
            binding.chronometer.text = String.format("00:%02d", seconds)

            viewModel.setPlayingStatus(false)
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error stopping playback: ${e.message}", e)
            showToast("Gagal menghentikan pemutaran: ${e.message}")
        }
    }

    private fun resetAudio() {
        try {
            audioFile?.let { AudioUtils.deleteRecording(it) }
            audioFile = null

            viewModel.setAudioFileUri(null)
            resetTimerDisplay()
            recordingTimer?.cancel()

            viewModel.setRecordingStatus(false)
            viewModel.setPlayingStatus(false)
            showToast("Audio direset")
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error resetting audio: ${e.message}", e)
            showToast("Gagal mereset audio: ${e.message}")
        }
    }

    private fun sendAudioToAPI(token: String) {
        if (audioFile == null || !audioFile!!.exists()) {
            showToast(getString(R.string.alert_empty_audio))
            return
        }

        val requestAudioFile = audioFile!!.asRequestBody("audio/wav".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "audio",
            audioFile!!.name,
            requestAudioFile
        )
        viewModel.voiceTest(token, multipartBody)
    }

    private fun setupObservers() {
        viewModel.testResult.observe(this) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(true)
                    result.data.prediction?.data?.let { prediction ->
                        moveToResult(
                            prediction.predictedEmotion.orEmpty(),
                            prediction.category.orEmpty(),
                            prediction.supportPercentage.toString()
                        )
                    }
                }


                is Resource.Error -> {
                    showLoading(false)
                    Log.e("VoiceTest", result.error)
                    showToast(result.error)
                }
            }
        }
    }

    private fun moveToResult(label: String, prediction: String, confidence: String) {
        val intent = Intent(this, MentalTestResultActivity::class.java).apply {
            putExtra(MentalTestResultActivity.EXTRA_TEST_NAME, "Voice Test")
            putExtra(MentalTestResultActivity.EXTRA_EMOTION_LABEL, label)
            putExtra(MentalTestResultActivity.EXTRA_PREDICTION, prediction)
            putExtra(MentalTestResultActivity.EXTRA_CONFIDENCE_PERCENTAGE, confidence)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        val loadingScreen = findViewById<View>(R.id.loadingLayout)
        loadingScreen.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.layoutVoiceTest.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecorder?.stopRecording()
        mediaPlayer?.release()
        recordingTimer?.cancel()
    }
}