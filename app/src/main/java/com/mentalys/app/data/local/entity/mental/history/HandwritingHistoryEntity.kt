package com.mentalys.app.data.local.entity.mental.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "handwriting_history")
@TypeConverters(Converters::class)
data class HandwritingHistoryEntity(
    @PrimaryKey val id: String,
    val prediction: HandwritingHistoryPredictionEntity?,
    val timestamp: String?
)

data class HandwritingHistoryPredictionEntity(
    val result: HandwritingHistoryPredictionResultEntity?,
)

data class HandwritingHistoryPredictionResultEntity(
    val status: String?,
    val result: String?,
    val confidence: Float?,
    val confidencePercentage: String?,
    val filename: String?
)