package com.mentalys.app.data.local.entity.mental.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mentalys.app.utils.Converters

@Entity(tableName = "quiz_history")
@TypeConverters(Converters::class)
data class QuizHistoryEntity(
    @PrimaryKey val id: String,
    val prediction: QuizHistoryPredictionEntity?,
    val inputData: QuizHistoryInputDataEntity?,
    val timestamp: String?
)

data class QuizHistoryPredictionEntity(
    val result: QuizHistoryPredictionResultEntity?
)

data class QuizHistoryPredictionResultEntity(
    val message: String?,
    val diagnosis: String?,
    val confidenceScore: Double?
)

data class QuizHistoryInputDataEntity(
    val increasedEnergy: Boolean?,
    val hopelessness: Boolean?,
    val poppingUpStressfulMemory: Boolean?,
    val blamingYourself: Boolean?,
    val seasonally: Boolean?,
    val introvert: Boolean?,
    val breathingRapidly: Boolean?,
    val feelingTired: Boolean?,
    val havingNightmares: Boolean?,
    val troubleInConcentration: Boolean?,
    val age: String?,
    val feelingNegative: Boolean?,
    val sweating: Boolean?,
    val socialMediaAddiction: Boolean?,
    val suicidalThought: Boolean?,
    val avoidsPeopleOrActivities: Boolean?,
    val changeInEating: Boolean?,
    val hallucinations: Boolean?,
    val anger: Boolean?,
    val overReact: Boolean?,
    val closeFriend: Boolean?,
    val panic: Boolean?,
    val feelingNervous: Boolean?,
    val weightGain: Boolean?,
    val repetitiveBehaviour: Boolean?,
    val troubleConcentrating: Boolean?,
    val havingTroubleWithWork: Boolean?,
    val havingTroubleInSleeping: Boolean?
)