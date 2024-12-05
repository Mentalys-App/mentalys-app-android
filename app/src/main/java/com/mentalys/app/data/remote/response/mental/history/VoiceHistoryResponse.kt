package com.mentalys.app.data.remote.response.mental.history

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryConfidenceScoresEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryPredictionEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryPredictionResultEntity

data class VoiceHistoryResponse(

    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("history")
    val history: List<VoiceItemResponse>,

    @field:SerializedName("total")
    val total: Int?,

    @field:SerializedName("page")
    val page: Int?,

    @field:SerializedName("limit")
    val limit: Int?,

    @field:SerializedName("totalPages")
    val totalPages: Int?

)

data class VoiceItemResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("prediction")
    val prediction: VoicePredictionResponse?,

    @field:SerializedName("inputData")
    val inputData: VoiceInputDataResponse?,

    @field:SerializedName("metadata")
    val metadata: VoiceMetadataResponse?,

    @field:SerializedName("timestamp")
    val timestamp: String?

) {
    fun toEntity(): VoiceHistoryEntity {
        return VoiceHistoryEntity(
            id = id,
            timestamp = timestamp,
            prediction = prediction?.toEntity()
        )
    }
}

data class VoicePredictionResponse(
    @field:SerializedName("result")
    val result: VoicePredictionResultResponse
) {
    fun toEntity(): VoiceHistoryPredictionEntity {
        return VoiceHistoryPredictionEntity(
            result = result.toEntity()
        )
    }
}

data class VoicePredictionResultResponse(

    @field:SerializedName("category")
    val category: String?,

    @field:SerializedName("predicted_emotion")
    val predictedEmotion: String?,

    @field:SerializedName("support_percentage")
    val supportPercentage: Int?,

    @field:SerializedName("confidence_scores")
    val confidenceScores: VoiceHistoryConfidenceScoresResponse?

) {
    fun toEntity(): VoiceHistoryPredictionResultEntity {
        return VoiceHistoryPredictionResultEntity(
            category = category,
            predictedEmotion = predictedEmotion,
            supportPercentage = supportPercentage,
            confidenceScores = confidenceScores?.toEntity()
        )
    }
}

data class VoiceHistoryConfidenceScoresResponse(

    @field:SerializedName("calm")
    val calm: Double?,

    @field:SerializedName("surprise")
    val surprise: Double?,

    @field:SerializedName("happy")
    val happy: Double?,

    @field:SerializedName("sad")
    val sad: Double?,

    @field:SerializedName("neutral")
    val neutral: Double?,

    @field:SerializedName("angry")
    val angry: Double?,

    @field:SerializedName("disgust")
    val disgust: Double?,

    @field:SerializedName("fear")
    val fear: Double?

) {
    fun toEntity(): VoiceHistoryConfidenceScoresEntity {
        return VoiceHistoryConfidenceScoresEntity(
            calm = calm,
            surprise = surprise,
            happy = happy,
            sad = sad,
            neutral = neutral,
            angry = angry,
            disgust = disgust,
            fear = fear
        )
    }
}

data class VoiceInputDataResponse(
    val any: Any? = null
)

data class VoiceMetadataResponse(
    val any: Any? = null
)