package com.mentalys.app.data.remote.response.mental.history

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryInputDataEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryPredictionEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryPredictionResultEntity

data class QuizHistoryResponse(

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("history")
    val history: List<QuizHistoryItemResponse>,

    @field:SerializedName("total")
    val total: Int?,

    @field:SerializedName("page")
    val page: Int?,

    @field:SerializedName("limit")
    val limit: Int?,

    @field:SerializedName("totalPages")
    val totalPages: Int?

)

data class QuizHistoryItemResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("prediction")
    val prediction: QuizHistoryPredictionResponse?,

    @field:SerializedName("inputData")
    val inputData: QuizHistoryInputDataResponse?,

    @field:SerializedName("metadata")
    val metadata: QuizHistoryMetadataResponse?,

    @field:SerializedName("timestamp")
    val timestamp: String?

) {
    fun toEntity(): QuizHistoryEntity {
        return QuizHistoryEntity(
            id = id,
            prediction = prediction?.toEntity(),
            inputData = inputData?.toEntity(),
            timestamp = timestamp,
        )
    }
}

data class QuizHistoryPredictionResponse(
    @field:SerializedName("result")
    val result: QuizHistoryPredictionResultResponse?
) {
    fun toEntity(): QuizHistoryPredictionEntity {
        return QuizHistoryPredictionEntity(
            result = result?.toEntity()
        )
    }
}

data class QuizHistoryPredictionResultResponse(

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("diagnosis")
    val diagnosis: String?,

    @field:SerializedName("confidence_score")
    val confidenceScore: Double?

) {
    fun toEntity(): QuizHistoryPredictionResultEntity {
        return QuizHistoryPredictionResultEntity(
            message = message,
            diagnosis = diagnosis,
            confidenceScore = confidenceScore
        )
    }
}

data class QuizHistoryInputDataResponse(
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
) {
    fun toEntity(): QuizHistoryInputDataEntity {
        return QuizHistoryInputDataEntity(
            increasedEnergy = increasedEnergy,
            hopelessness = hopelessness,
            poppingUpStressfulMemory = poppingUpStressfulMemory,
            blamingYourself = blamingYourself,
            seasonally = seasonally,
            introvert = introvert,
            breathingRapidly = breathingRapidly,
            feelingTired = feelingTired,
            havingNightmares = havingNightmares,
            troubleInConcentration = troubleInConcentration,
            age = age,
            feelingNegative = feelingNegative,
            sweating = sweating,
            socialMediaAddiction = socialMediaAddiction,
            suicidalThought = suicidalThought,
            avoidsPeopleOrActivities = avoidsPeopleOrActivities,
            changeInEating = changeInEating,
            hallucinations = hallucinations,
            anger = anger,
            overReact = overReact,
            closeFriend = closeFriend,
            panic = panic,
            feelingNervous = feelingNervous,
            weightGain = weightGain,
            repetitiveBehaviour = repetitiveBehaviour,
            troubleConcentrating = troubleConcentrating,
            havingTroubleWithWork = havingTroubleWithWork,
            havingTroubleInSleeping = havingTroubleInSleeping
        )
    }
}

data class QuizHistoryMetadataResponse(
    val any: Any? = null
)