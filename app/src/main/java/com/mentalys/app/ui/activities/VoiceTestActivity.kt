package com.mentalys.app.ui.activities

import AudioRecorder
import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityVoiceTestBinding
import com.mentalys.app.ui.fragments.QuizTestPage1Fragment
import com.mentalys.app.ui.viewmodels.ViewModelFactory
import com.mentalys.app.ui.viewmodels.VoiceTestViewModel
import com.mentalys.app.utils.AudioUtils
import com.mentalys.app.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class VoiceTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVoiceTestBinding
    private val viewModel: VoiceTestViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var mediaRecorder: MediaRecorder? = null
    private var audioRecorder: AudioRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFile: File? = null
    private var audioUri: Uri? = null
    private var isRecording = false
    private var isPlaying = false
    private var recordingDuration = 0L
    private var countDownTimer: CountDownTimer? = null
    private var isChronometerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoiceTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()
        observeViewModel()
        setupListeners()
        updateUI()
        setupObservers()
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
        val token =
            ""
        binding.buttonResetAudio.setOnClickListener { resetAudio() }
        binding.buttonSendAudio.setOnClickListener {
            sendAudioToAPI(token)
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
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.start()
            isChronometerRunning = true

            viewModel.setAudioFileUri(Uri.fromFile(audioFile))
            viewModel.setRecordingStatus(true)
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error starting recording: ${e.message}", e)
            showToast("Gagal memulai rekaman: ${e.message}")
        }
    }

    private fun stopRecording() {
        try {
            audioRecorder?.stopRecording()
            audioRecorder = null

            if (isChronometerRunning) {
                recordingDuration = SystemClock.elapsedRealtime() - binding.chronometer.base
                binding.chronometer.stop()
                isChronometerRunning = false
            }

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

            Log.d("VoiceTest", "Playing audio from: ${audioFile!!.absolutePath}")
            Log.d("VoiceTest", "Audio file exists: ${audioFile!!.exists()}")
            Log.d("VoiceTest", "Audio file size: ${audioFile!!.length()} bytes")

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

            startCountdownTimer(recordingDuration)
            viewModel.setPlayingStatus(true)
        } catch (e: Exception) {
            Log.e("VoiceTest", "Error playing audio: ${e.message}", e)
            showToast("Gagal memutar audio: ${e.message}")
        }
    }

    private fun stopPlayback() {
        try {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null

            countDownTimer?.cancel()
            countDownTimer = null

            val totalSeconds = (recordingDuration / 1000).toInt()
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            binding.chronometer.text = String.format("%02d:%02d", minutes, seconds)

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
            binding.chronometer.text = "00:00"
            countDownTimer?.cancel()
            countDownTimer = null

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

        Log.d("VoiceTest", "Audio file size before sending: ${audioFile!!.length()} bytes")
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
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(true)
                    val prediction = result.data.prediction.data
                    moveToResult(
                        prediction.predictedEmotion,
                        prediction.category,
                        prediction.supportPercentage.toString()
                    )
                }
                is Result.Error -> {
                    showLoading(false)
                    Log.e("VoiceTest", result.error)
                    showToast(result.error)
                }
            }
        }
    }


    private fun moveToResult(label: String, prediction: String, confidence: String) {
        val intent = Intent(this, TestResultActivity::class.java).apply {
            putExtra(TestResultActivity.EXTRA_TEST_NAME, "Voice Test")
            putExtra(TestResultActivity.EXTRA_EMOTION_LABEL, label)
            putExtra(TestResultActivity.EXTRA_PREDICTION, prediction)
            putExtra(TestResultActivity.EXTRA_CONFIDENCE_PERCENTAGE, confidence)
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

    private fun startCountdownTimer(duration: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.chronometer.text =
                    String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
            }

            override fun onFinish() {
                binding.chronometer.text = "00:00"
            }
        }.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecorder?.stopRecording()
//        mediaRecorder?.release()
        mediaPlayer?.release()
        countDownTimer?.cancel()
    }
}