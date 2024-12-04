import com.google.gson.annotations.SerializedName

data class QuizTestResponse(

	@SerializedName("status")
	val status: String,

	@SerializedName("prediction")
	val prediction: QuizTestPredictionResponse

)

data class QuizTestPredictionResponse(

	@SerializedName("status")
	val status: String,

	@SerializedName("data")
	val data: QuizTestPredictionDataResponse

)

data class QuizTestPredictionDataResponse(

	@SerializedName("confidence_score")
	val confidenceScore: Double,

	@SerializedName("diagnosis")
	val diagnosis: String,

	@SerializedName("message")
	val message: String
)
