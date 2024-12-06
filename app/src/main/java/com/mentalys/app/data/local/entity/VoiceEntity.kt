package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "voice_history")
@TypeConverters(Converters::class)
data class VoiceEntity(
    @PrimaryKey val id: String,
    val prediction: VoicePredictionEntity?,
    val timestamp: String?
)

data class VoicePredictionEntity(
    val result: VoicePredictionResultEntity?,
)

data class VoicePredictionResultEntity(
    val result: String?,
    val confidencePercentage: String?,
)