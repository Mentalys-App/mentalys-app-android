import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import android.Manifest
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import android.util.Log

class AudioRecorder(private val context: Context, private val outputFilePath: String) {

    private val sampleRate = 22050
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    private var audioRecord: AudioRecord? = null
    private var isRecording = false

    fun startRecording() {
        Log.d("AudioRecorder", "Starting recording to: $outputFilePath")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException("Audio recording permission not granted")
        }

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        audioRecord?.startRecording()
        isRecording = true

        Thread {
            writeAudioDataToFile()
        }.start()
    }

    fun stopRecording() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun writeAudioDataToFile() {
        val tempFile = File("$outputFilePath.pcm")
        val fos = FileOutputStream(tempFile)
        val buffer = ByteArray(bufferSize)

        while (isRecording) {
            val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
            if (read > 0) {
                fos.write(buffer, 0, read)
            }
        }

        fos.close()
        convertPcmToWav(tempFile, File(outputFilePath))
        tempFile.delete()
    }

    private fun convertPcmToWav(pcmFile: File, wavFile: File) {
        try {
            val pcmData = pcmFile.readBytes()

            // WAV header parameters
            val sampleRate = 22050
            val bitsPerSample = 16
            val channels = 1
            val byteRate = sampleRate * channels * bitsPerSample / 8
            val blockAlign = channels * bitsPerSample / 8

            // Buat header WAV
            val header = ByteArray(44)
            header[0] = 'R'.code.toByte() // RIFF
            header[1] = 'I'.code.toByte()
            header[2] = 'F'.code.toByte()
            header[3] = 'F'.code.toByte()

            // Ukuran file total (header + data)
            val totalDataLen = pcmData.size + 36
            header[4] = (totalDataLen and 0xff).toByte()
            header[5] = ((totalDataLen shr 8) and 0xff).toByte()
            header[6] = ((totalDataLen shr 16) and 0xff).toByte()
            header[7] = ((totalDataLen shr 24) and 0xff).toByte()

            header[8] = 'W'.code.toByte() // WAVE
            header[9] = 'A'.code.toByte()
            header[10] = 'V'.code.toByte()
            header[11] = 'E'.code.toByte()

            header[12] = 'f'.code.toByte() // fmt
            header[13] = 'm'.code.toByte()
            header[14] = 't'.code.toByte()
            header[15] = ' '.code.toByte()

            header[16] = 16 // PCM header length
            header[17] = 0
            header[18] = 0
            header[19] = 0

            header[20] = 1 // Audio format (1 = PCM)
            header[21] = 0

            header[22] = channels.toByte() // Channels
            header[23] = 0

            // Sample rate
            header[24] = (sampleRate and 0xff).toByte()
            header[25] = ((sampleRate shr 8) and 0xff).toByte()
            header[26] = ((sampleRate shr 16) and 0xff).toByte()
            header[27] = ((sampleRate shr 24) and 0xff).toByte()

            // Byte rate
            header[28] = (byteRate and 0xff).toByte()
            header[29] = ((byteRate shr 8) and 0xff).toByte()
            header[30] = ((byteRate shr 16) and 0xff).toByte()
            header[31] = ((byteRate shr 24) and 0xff).toByte()

            header[32] = blockAlign.toByte() // Block align
            header[33] = 0

            header[34] = bitsPerSample.toByte() // Bits per sample
            header[35] = 0

            header[36] = 'd'.code.toByte() // data
            header[37] = 'a'.code.toByte()
            header[38] = 't'.code.toByte()
            header[39] = 'a'.code.toByte()

            // Ukuran data PCM
            header[40] = (pcmData.size and 0xff).toByte()
            header[41] = ((pcmData.size shr 8) and 0xff).toByte()
            header[42] = ((pcmData.size shr 16) and 0xff).toByte()
            header[43] = ((pcmData.size shr 24) and 0xff).toByte()

            // Tulis file WAV
            FileOutputStream(wavFile).use { fos ->
                fos.write(header)
                fos.write(pcmData)
            }

            Log.d("AudioRecorder", "WAV conversion successful")
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error converting PCM to WAV", e)
        }
    }
}
