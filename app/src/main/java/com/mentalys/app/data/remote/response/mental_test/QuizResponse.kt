package com.mentalys.app.data.remote.response.mental_test

import com.google.gson.annotations.SerializedName

data class QuizResponse(
	@field:SerializedName("prediction")
	val prediction: QuizPrediction?,

	@field:SerializedName("status")
	val status: String
)

data class QuizData(

	@field:SerializedName("confidence_score")
	val confidenceScore: Any? = null,

	@field:SerializedName("diagnosis")
	val diagnosis: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class QuizPrediction(
	@field:SerializedName("data")
	val data: QuizData? = null,

	@field:SerializedName("status")
	val status: String? = null
)
