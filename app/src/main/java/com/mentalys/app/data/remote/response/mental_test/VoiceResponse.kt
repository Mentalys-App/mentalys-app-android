package com.mentalys.app.data.remote.response.mental_test

import com.google.gson.annotations.SerializedName

data class VoiceResponse(

	@field:SerializedName("prediction")
	val prediction: Prediction,

	@field:SerializedName("status")
	val status: String
)

data class VoiceData(

	@field:SerializedName("predicted_emotion")
	val predictedEmotion: String,

	@field:SerializedName("support_percentage")
	val supportPercentage: Any,

	@field:SerializedName("confidence_scores")
	val confidenceScores: ConfidenceScores,

	@field:SerializedName("category")
	val category: String
)

data class ConfidenceScores(

	@field:SerializedName("calm")
	val calm: Any,

	@field:SerializedName("surprise")
	val surprise: Any,

	@field:SerializedName("happy")
	val happy: Any,

	@field:SerializedName("sad")
	val sad: Any,

	@field:SerializedName("neutral")
	val neutral: Any,

	@field:SerializedName("angry")
	val angry: Any,

	@field:SerializedName("disgust")
	val disgust: Any,

	@field:SerializedName("fear")
	val fear: Any
)

data class Prediction(

	@field:SerializedName("data")
	val data: VoiceData,

	@field:SerializedName("status")
	val status: String
)
