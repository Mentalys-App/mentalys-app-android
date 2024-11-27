package com.mentalys.app.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import com.mentalys.app.data.remote.response.article.Author
import com.mentalys.app.data.remote.response.article.Content
import com.mentalys.app.data.remote.response.article.Metadata
import com.mentalys.app.data.local.entity.AuthorEntity
import com.mentalys.app.data.local.entity.ContentEntity
import com.mentalys.app.data.local.entity.MetadataEntity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun mapAuthorToEntity(author: Author): AuthorEntity {
    return AuthorEntity(
        id = author.id,
        name = author.name,
        profileImage = author.profileImage,
        bio = author.bio
    )
}

fun mapMetadataToEntity(metadata: Metadata): MetadataEntity {
    return MetadataEntity(
        publishDate = metadata.publishDate,
        lastUpdated = metadata.lastUpdated,
        tags = metadata.tags,
        category = metadata.category,
        readingTime = metadata.readingTime,
        likes = metadata.likes,
        views = metadata.views
    )
}

fun mapContentListToEntity(contentList: List<Content>): List<ContentEntity> {
    return contentList.map { content ->
        ContentEntity(
            type = content.type,
            level = content.level,
            text = content.text,
            src = content.src,
            caption = content.caption,
            alText = content.altText,
            author = content.author,
            authorRole = content.authorRole,
            style = content.style,
            items = content.items,
            platform = content.platform,
            url = content.url,
            description = content.description
        )
    }
}

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
