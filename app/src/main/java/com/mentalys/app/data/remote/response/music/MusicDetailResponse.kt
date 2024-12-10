package com.mentalys.app.data.remote.response.music

import com.mentalys.app.data.local.entity.ImagesEntity
import com.mentalys.app.data.local.entity.MusicDetailEntity
import com.mentalys.app.data.local.entity.PreviewsEntity

data class MusicDetailResponse(
    val id: Int,
    val url: String,
    val name: String,
    val tags: List<String>,
    val description: String?,
    val geotag: String?,
    val created: String,
    val license: String,
    val type: String,
    val channels: Int,
    val filesize: Int,
    val bitrate: Int,
    val bitdepth: Int,
    val duration: Double,
    val samplerate: Double,
    val username: String,
    val pack: String?,
    val pack_name: String?,
    val download: String,
    val bookmark: String,
    val previews: PreviewsResponse,
    val images: ImagesResponse,
    val num_downloads: Int,
    val avg_rating: Double,
    val num_ratings: Int,
    val rate: String,
    val comments: String,
    val num_comments: Int,
    val comment: String,
    val similar_sounds: String,
    val analysis: String,
    val analysis_frames: String,
    val analysis_stats: String,
    val is_explicit: Boolean
) {
    fun toEntity(): MusicDetailEntity {
        return MusicDetailEntity(
            id = id,
            url = url,
            name = name,
            tags = tags,
            description = description,
            geotag = geotag,
            created = created,
            license = license,
            type = type,
            channels = channels,
            filesize = filesize,
            bitrate = bitrate,
            bitdepth = bitdepth,
            duration = duration,
            samplerate = samplerate,
            username = username,
            pack = pack,
            pack_name = pack_name,
            download = download,
            bookmark = bookmark,
            previews = previews.toEntity(),
            images = images.toEntity(),
            num_downloads = num_downloads,
            avg_rating = avg_rating,
            num_ratings = num_ratings,
            is_explicit = is_explicit,
            rate = rate,
            comments = comments,
            num_comments = num_comments,
            comment = comment,
            similar_sounds = similar_sounds,
            analysis = analysis,
            analysis_frames = analysis_frames,
            analysis_stats = analysis_stats
        )
    }
}

data class PreviewsResponse(
    val previewHqMp3: String,
    val previewHqOgg: String,
    val previewLqMp3: String,
    val previewLqOgg: String
) {
    fun toEntity(): PreviewsEntity {
        return PreviewsEntity(
            previewHqMp3 = previewHqMp3,
            previewHqOgg = previewHqOgg,
            previewLqMp3 = previewLqMp3,
            previewLqOgg = previewLqOgg
        )
    }
}

data class ImagesResponse(
    val waveformM: String,
    val waveformL: String,
    val spectralM: String,
    val spectralL: String,
    val waveformBwM: String,
    val waveformBwL: String,
    val spectralBwM: String,
    val spectralBwL: String
) {
    fun toEntity(): ImagesEntity {
        return ImagesEntity(
            waveformM = waveformM,
            waveformL = waveformL,
            spectralM = spectralM,
            spectralL = spectralL,
            waveformBwM = waveformBwM,
            waveformBwL = waveformBwL,
            spectralBwM = spectralBwM,
            spectralBwL = spectralBwL
        )
    }
}
