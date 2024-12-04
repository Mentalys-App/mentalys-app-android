package com.mentalys.app.data.remote.response.mental.test

import com.google.gson.annotations.SerializedName

data class VoiceTestResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("prediction")
    val prediction: VoiceTestPredictionResponse? = null

)

data class VoiceTestPredictionResponse(

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: VoiceTestDataResponse? = null

)

data class VoiceTestDataResponse(

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("predicted_emotion")
    val predictedEmotion: String? = null,

    @field:SerializedName("support_percentage")
    val supportPercentage: Double? = null,

    @field:SerializedName("confidence_scores")
    val confidenceScores: VoiceTestConfidenceScoresResponse? = null

)


data class VoiceTestConfidenceScoresResponse(

    @field:SerializedName("calm")
    val calm: Double? = null,

    @field:SerializedName("surprise")
    val surprise: Double? = null,

    @field:SerializedName("happy")
    val happy: Double? = null,

    @field:SerializedName("sad")
    val sad: Double? = null,

    @field:SerializedName("neutral")
    val neutral: Double? = null,

    @field:SerializedName("angry")
    val angry: Double? = null,

    @field:SerializedName("disgust")
    val disgust: Double? = null,

    @field:SerializedName("fear")
    val fear: Double? = null

)