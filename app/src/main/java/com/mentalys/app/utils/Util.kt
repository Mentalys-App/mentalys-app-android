package com.mentalys.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.exifinterface.media.ExifInterface
import com.mentalys.app.data.remote.response.mental.AudioResult
import com.mentalys.app.data.remote.response.mental.HandwritingResult
import com.mentalys.app.data.remote.response.mental.HistoryItem
import com.mentalys.app.data.remote.response.mental.QuizResult
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun showToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

//Camera X
private const val MAXIMAL_SIZE = 1000000 //1 MB
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())


fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}

//mapper history mental test
fun mapHistoryItems(historyItems: List<HistoryItem>): List<HistoryItem> {
    return historyItems.map { historyItem ->
        val mappedResult = when (historyItem.type) {
            "audio" -> parseAudioResult(historyItem.prediction.result)
            "handwriting" -> parseHandwritingResult(historyItem.prediction.result)
            "quiz" -> parseQuizResult(historyItem.prediction.result)
            else -> null
        }
        historyItem.copy(prediction = historyItem.prediction.copy(result = mappedResult))
    }
}

private fun parseAudioResult(data: Any?): AudioResult? {
    val resultJson = data as? Map<String, Any>
    return resultJson?.let {
        AudioResult(
            confidence_scores = it["confidence_scores"] as? Map<String, Double>,
            predicted_emotion = it["predicted_emotion"] as? String,
            support_percentage = it["support_percentage"] as? Double,
            category = it["category"] as? String
        )
    }
}

private fun parseHandwritingResult(data: Any?): HandwritingResult? {
    val resultJson = data as? Map<String, Any>
    return resultJson?.let {
        HandwritingResult(
            confidence_percentage = it["confidence_percentage"] as? String,
            result = it["result"] as? String,
            status = it["status"] as? String,
            confidence = it["confidence"] as? Double,
            filename = it["filename"] as? String
        )
    }
}

private fun parseQuizResult(data: Any?): QuizResult? {
    val resultJson = data as? Map<String, Any>
    return resultJson?.let {
        QuizResult(
            diagnosis = it["diagnosis"] as? String,
            confidence_score = it["confidence_score"] as? Double,
            message = it["message"] as? String
        )
    }
}

fun formatTimestamp(timestamp: String): String {
    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return try {
        val date = isoFormatter.parse(timestamp)
        val outputFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        outputFormatter.format(date ?: return "Invalid Date")
    } catch (e: ParseException) {
        "Invalid Date"
    }
}


//AUDIO
object AudioUtils {
    private const val AUDIO_FOLDER = "audio_recordings"

    fun createAudioFile(context: Context, timestamp: Long, extension: String = ".wav"): File? {
        return try {
            val directory = File(context.getExternalFilesDir(null), AUDIO_FOLDER)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            File(directory, "voice_test_$timestamp$extension")
        } catch (e: Exception) {
            Log.e("AudioUtils", "Error creating audio file: ${e.message}", e)
            null
        }
    }

    fun deleteRecording(file: File) {
        try {
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            Log.e("AudioUtils", "Error deleting recording: ${e.message}", e)
        }
    }
}

data class AudioRecording(
    val file: File,
    val timestamp: Long,
    val duration: Long
)

fun getErrorMessage(e: Exception): String {
    return when (e) {
        is UnknownHostException -> "No internet connection. Please check your network."
        is SocketTimeoutException -> "The request timed out. Please try again."
        else -> e.message ?: "An unexpected error occurred."
    }
}

fun openAndroidXBrowser(context: Context, url: String) {
    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setShareState(CustomTabsIntent.SHARE_STATE_ON)
        .build()

    try {
        customTabsIntent.launchUrl(context, Uri.parse(url))
    } catch (e: Exception) {
        showToast(context, "Unable to open the link. Please try again.")
    }
}

fun formatToIDR(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(amount)
}