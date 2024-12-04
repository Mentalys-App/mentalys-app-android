package com.mentalys.app.data.remote.response.history

import com.google.gson.annotations.SerializedName

data class HandwritngHistoryResponse(
	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("limit")
	val limit: Int,

	@field:SerializedName("totalPages")
	val totalPages: Int,

	@field:SerializedName("history")
	val handwritingHistory: List<HandwritingHistoryItem>,

	@field:SerializedName("page")
	val page: Int,

	@field:SerializedName("status")
	val status: String
)

data class Result(
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

data class HandwritingHistoryItem(

	@field:SerializedName("inputData")
	val inputData: InputData,

	@field:SerializedName("metadata")
	val metadata: Metadata,

	@field:SerializedName("prediction")
	val prediction: Prediction,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("timestamp")
	val timestamp: String
)

data class InputData(
	val any: Any? = null
)

data class Prediction(
	@field:SerializedName("result")
	val result: Result
)

data class Metadata(
	val any: Any? = null
)
