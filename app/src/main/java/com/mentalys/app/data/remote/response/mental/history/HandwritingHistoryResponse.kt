package com.mentalys.app.data.remote.response.mental.history

import com.google.gson.annotations.SerializedName
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryPredictionEntity
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryPredictionResultEntity

data class HandwritingResponse(

	@field:SerializedName("status")
	val status: String?,

	@field:SerializedName("history")
	val history: List<HandwritingItemResponse>,

	@field:SerializedName("total")
	val total: Int?,

	@field:SerializedName("page")
	val page: Int?,

	@field:SerializedName("limit")
	val limit: Int?,

	@field:SerializedName("totalPages")
	val totalPages: Int?

)

data class HandwritingItemResponse(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("prediction")
	val prediction: HandwritingPredictionResponse?,

	@field:SerializedName("inputData")
	val inputData: HandwritingInputDataResponse?,

	@field:SerializedName("metadata")
	val metadata: HandwritingMetadataResponse?,

	@field:SerializedName("timestamp")
	val timestamp: String?

) {
	fun toEntity(): HandwritingHistoryEntity {
		return HandwritingHistoryEntity(
			id = this.id,
			prediction = this.prediction?.toEntity(),
			timestamp = this.timestamp
		)
	}
}

data class HandwritingPredictionResponse(
	@field:SerializedName("result")
	val result: HandwritingPredictionResultResponse
) {
	fun toEntity(): HandwritingHistoryPredictionEntity {
		return HandwritingHistoryPredictionEntity(
			result = this.result.toEntity()
		)
	}
}

data class HandwritingPredictionResultResponse(

	@field:SerializedName("status")
	val status: String?,

	@field:SerializedName("result")
	val result: String?,

	@field:SerializedName("confidence")
	val confidence: Float?,

	@field:SerializedName("confidence_percentage")
	val confidencePercentage: String?,

	@field:SerializedName("filename")
	val filename: String?

) {
	fun toEntity(): HandwritingHistoryPredictionResultEntity {
		return HandwritingHistoryPredictionResultEntity(
			status = this.status,
			result = this.result,
			confidence = this.confidence,
			confidencePercentage = this.confidencePercentage,
			filename = this.filename
		)
	}
}

data class HandwritingInputDataResponse(
	val any: Any? = null
)

data class HandwritingMetadataResponse(
	val any: Any? = null
)

