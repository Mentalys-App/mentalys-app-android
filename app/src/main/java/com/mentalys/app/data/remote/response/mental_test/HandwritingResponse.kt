package com.mentalys.app.data.remote.response.mental_test

import com.google.gson.annotations.SerializedName

data class HandwritingResponse(

	@field:SerializedName("prediction")
	val prediction: HandwritingPrediction,

	@field:SerializedName("status")
	val status: String
)

data class HandwritingPrediction(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("filename")
	val filename: String,

	@field:SerializedName("confidence_percentage")
	val confidencePercentage: String,

	@field:SerializedName("confidence")
	val confidence: Any,

	@field:SerializedName("status")
	val status: String
)
