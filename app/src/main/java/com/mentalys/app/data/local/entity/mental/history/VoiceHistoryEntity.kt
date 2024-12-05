package com.mentalys.app.data.local.entity.mental.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "voice_history")
@TypeConverters(Converters::class)
data class VoiceHistoryEntity(
    @PrimaryKey val id: String,
    val prediction: VoiceHistoryPredictionEntity?,
    val timestamp: String?
)

data class VoiceHistoryPredictionEntity(
    val result: VoiceHistoryPredictionResultEntity?,
)

data class VoiceHistoryPredictionResultEntity(
    val category: String?,
    val predictedEmotion: String?,
    val supportPercentage: Int?,
    val confidenceScores: VoiceHistoryConfidenceScoresEntity?
)

data class VoiceHistoryConfidenceScoresEntity(
    val calm: Double?,
    val surprise: Double?,
    val happy: Double?,
    val sad: Double?,
    val neutral: Double?,
    val angry: Double?,
    val disgust: Double?,
    val fear: Double?
)