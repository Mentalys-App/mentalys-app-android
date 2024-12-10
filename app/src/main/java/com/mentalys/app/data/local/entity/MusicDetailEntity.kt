package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "music")
@TypeConverters(Converters::class)
data class MusicDetailEntity(
    @PrimaryKey val id: Int,
    val url: String?,
    val name: String?,
    val tags: List<String>?,
    val description: String??,
    val geotag: String??,
    val created: String?,
    val license: String?,
    val type: String?,
    val channels: Int?,
    val filesize: Int?,
    val bitrate: Int?,
    val bitdepth: Int?,
    val duration: Double?,
    val samplerate: Double?,
    val username: String?,
    val pack: String??,
    val pack_name: String??,
    val download: String?,
    val bookmark: String?,
    val previews: PreviewsEntity?,
    val images: ImagesEntity?,
    val num_downloads: Int?,
    val avg_rating: Double?,
    val num_ratings: Int?,
    val rate: String?,
    val comments: String?,
    val num_comments: Int?,
    val comment: String?,
    val similar_sounds: String?,
    val analysis: String?,
    val analysis_frames: String?,
    val analysis_stats: String?,
    val is_explicit: Boolean?
)

data class PreviewsEntity(
    val previewHqMp3: String?,
    val previewHqOgg: String?,
    val previewLqMp3: String?,
    val previewLqOgg: String?
)

data class ImagesEntity(
    val waveformM: String?,
    val waveformL: String?,
    val spectralM: String?,
    val spectralL: String?,
    val waveformBwM: String?,
    val waveformBwL: String?,
    val spectralBwM: String?,
    val spectralBwL: String?
)

