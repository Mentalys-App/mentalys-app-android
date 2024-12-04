package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "handwriting_history")
@TypeConverters(Converters::class)
data class HandwritingEntity(
    @PrimaryKey val id: String,
    val prediction: HandwritingPredictionEntity?,
    val timestamp: String?
)

data class HandwritingPredictionEntity(
    val result: HandwritingPredictionResultEntity?,
)

data class HandwritingPredictionResultEntity(
    val status: String?,
    val result: String?,
    val confidence: Float?,
    val confidencePercentage: String?,
    val filename: String?
)