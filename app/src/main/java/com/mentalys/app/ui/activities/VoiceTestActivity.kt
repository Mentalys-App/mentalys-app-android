package com.mentalys.app.ui.activities

import android.Manifest
import android.content.ContentValues
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mentalys.app.R
import com.mentalys.app.databinding.ActivityVoiceTestBinding
import com.mentalys.app.ui.viewmodels.VoiceTestViewModel
import java.io.File
import java.io.FileDescriptor
import java.io.OutputStream

class VoiceTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVoiceTestBinding
    private val viewModel: VoiceTestViewModel by viewModels()

    private var mediaRecorder: MediaRecorder? = null
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
        binding.buttonSendAudio.setOnClickListener { sendAudioToAPI() }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startRecordingUsingMediaStore()
        } else {
            startRecordingUsingFile()
        }
    }

    private fun startRecordingUsingMediaStore() {
        val timestamp = System.currentTimeMillis()
        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, "voice_test_$timestamp.3gp")
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp")
        }
        val audioUri = contentResolver.insert(
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            contentValues
        )
        this.audioUri = audioUri ?: return

        try {
            contentResolver.openFileDescriptor(audioUri, "w")?.use { parcelFileDescriptor ->
                val fileDescriptor = parcelFileDescriptor.fileDescriptor
                startMediaRecorder(fileDescriptor)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal memulai rekaman: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun startRecordingUsingFile() {
        val timestamp = System.currentTimeMillis()
        audioFile = File(Environment.getExternalStorageDirectory(), "voice_test_$timestamp.3gp")
        startMediaRecorder(null)
    }

    private fun startMediaRecorder(fileDescriptor: FileDescriptor?) {
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                if (fileDescriptor != null) {
                    setOutputFile(fileDescriptor)
                } else {
                    setOutputFile(audioFile?.absolutePath)
                }
                prepare()
                start()
            }

            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.start()
            isChronometerRunning = true

            viewModel.setAudioFileUri(audioUri ?: Uri.fromFile(audioFile))
            viewModel.setRecordingStatus(true)
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal memulai rekaman: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            if (isChronometerRunning) {
                recordingDuration = SystemClock.elapsedRealtime() - binding.chronometer.base
                binding.chronometer.stop()
                isChronometerRunning = false
            }

            viewModel.setRecordingStatus(false)
            Toast.makeText(this, "Rekaman selesai", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal menghentikan rekaman: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAudio() {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@VoiceTestActivity, audioUri ?: Uri.fromFile(audioFile))
                prepare()
                start()
                setOnCompletionListener { stopPlayback() }
            }

            // Start countdown timer
            startCountdownTimer(recordingDuration)

            isPlaying = true
            updateUI()
        } catch (e: Exception) {
            Log.d("AudioPlayback", "Gagal memutar audio: ${e.message}")
            Toast.makeText(this, "Gagal memutar audio: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun stopPlayback() {
        try {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null

            // Stop countdown timer
            countDownTimer?.cancel()
            countDownTimer = null

            // Kembalikan waktu ke durasi rekaman sebelumnya
            val totalSeconds = (recordingDuration / 1000).toInt()
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            binding.chronometer.text = String.format("%02d:%02d", minutes, seconds)


            isPlaying = false
            updateUI()
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal menghentikan pemutaran: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetAudio() {
        try {
            audioFile?.delete()
            audioFile = null

            viewModel.setAudioFileUri(null)

            binding.chronometer.text = "00:00"
            countDownTimer?.cancel()
            countDownTimer = null

            viewModel.setRecordingStatus(false)
            viewModel.setPlayingStatus(false)
            Toast.makeText(this, "Audio direset", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal mereset audio: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendAudioToAPI() {
        if (audioFile == null) {
            Toast.makeText(this, "Tidak ada audio untuk dikirim", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Audio dikirim: ${audioFile?.name}", Toast.LENGTH_SHORT).show()
    }

    private fun startCountdownTimer(duration: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.chronometer.text = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
            }

            override fun onFinish() {
                binding.chronometer.text = "00:00"
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaPlayer?.release()
        countDownTimer?.cancel()
    }
}
