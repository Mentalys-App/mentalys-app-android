package com.mentalys.app.data.remote.response.mental.history

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.HandwritingEntity
import com.mentalys.app.data.local.entity.HandwritingPredictionEntity
import com.mentalys.app.data.local.entity.HandwritingPredictionResultEntity
import com.mentalys.app.data.local.entity.QuizEntity
import com.mentalys.app.data.local.entity.QuizPredictionEntity
import com.mentalys.app.data.local.entity.QuizPredictionResultEntity

data class QuizResponse(

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("history")
    val history: List<QuizItemResponse>,

    @field:SerializedName("total")
    val total: Int?,

    @field:SerializedName("page")
    val page: Int?,

    @field:SerializedName("limit")
    val limit: Int?,

    @field:SerializedName("totalPages")
    val totalPages: Int?

)

data class QuizItemResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("prediction")
    val prediction: QuizPredictionResponse?,

    @field:SerializedName("inputData")
    val inputData: QuizInputDataResponse?,

    @field:SerializedName("metadata")
    val metadata: QuizMetadataResponse?,

    @field:SerializedName("timestamp")
    val timestamp: String?
){
    fun toEntity(): QuizEntity {
        return QuizEntity(
            id = this.id,
            prediction = this.prediction?.toEntity(),
            timestamp = this.timestamp
        )
    }
}

data class QuizPredictionResponse(
    @field:SerializedName("result")
    val result: QuizPredictionResultResponse
){
    fun toEntity(): QuizPredictionEntity {
        return QuizPredictionEntity(
            result = this.result.toEntity()
        )
    }
}

data class QuizPredictionResultResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("diagnosis")
    val diagnosis: String?,

    @field:SerializedName("confidence_score")
    val confidenceScore: Double?

){
    fun toEntity(): QuizPredictionResultEntity {
        return QuizPredictionResultEntity(
            result = this.diagnosis,
            confidencePercentage = this.confidenceScore.toString(),
        )
    }
}

data class QuizInputDataResponse(
    @field:SerializedName("increased_energy") val increasedEnergy: Boolean?,
    @field:SerializedName("hopelessness") val hopelessness: Boolean?,
    @field:SerializedName("popping_up_stressful_memory") val poppingUpStressfulMemory: Boolean?,
    @field:SerializedName("blaming_yourself") val blamingYourself: Boolean?,
    @field:SerializedName("seasonally") val seasonally: Boolean?,
    @field:SerializedName("introvert") val introvert: Boolean?,
    @field:SerializedName("breathing_rapidly") val breathingRapidly: Boolean?,
    @field:SerializedName("feeling_tired") val feelingTired: Boolean?,
    @field:SerializedName("having_nightmares") val havingNightmares: Boolean?,
    @field:SerializedName("trouble_in_concentration") val troubleInConcentration: Boolean?,
    @field:SerializedName("age") val age: String?,
    @field:SerializedName("feeling_negative") val feelingNegative: Boolean?,
    @field:SerializedName("sweating") val sweating: Boolean?,
    @field:SerializedName("social_media_addiction") val socialMediaAddiction: Boolean?,
    @field:SerializedName("suicidal_thought") val suicidalThought: Boolean?,
    @field:SerializedName("avoids_people_or_activities") val avoidsPeopleOrActivities: Boolean?,
    @field:SerializedName("change_in_eating") val changeInEating: Boolean?,
    @field:SerializedName("hallucinations") val hallucinations: Boolean?,
    @field:SerializedName("anger") val anger: Boolean?,
    @field:SerializedName("over_react") val overReact: Boolean?,
    @field:SerializedName("close_friend") val closeFriend: Boolean?,
    @field:SerializedName("panic") val panic: Boolean?,
    @field:SerializedName("feeling_nervous") val feelingNervous: Boolean?,
    @field:SerializedName("weight_gain") val weightGain: Boolean?,
    @field:SerializedName("repetitive_behaviour") val repetitiveBehaviour: Boolean?,
    @field:SerializedName("trouble_concentrating") val troubleConcentrating: Boolean?,
    @field:SerializedName("having_trouble_with_work") val havingTroubleWithWork: Boolean?,
    @field:SerializedName("having_trouble_in_sleeping") val havingTroubleInSleeping: Boolean?
)

data class QuizMetadataResponse(
    val any: Any? = null
)