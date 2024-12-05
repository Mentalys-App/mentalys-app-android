package com.mentalys.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "quiz_history")
@TypeConverters(Converters::class)
data class QuizEntity(
    @PrimaryKey val id: String,
    val prediction: QuizPredictionEntity?,
    val timestamp: String?
)

data class QuizPredictionEntity(
    val result: QuizPredictionResultEntity?,
)

data class QuizPredictionResultEntity(
    val result: String?,
    val confidencePercentage: String?,
)