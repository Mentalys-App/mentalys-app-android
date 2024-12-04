package com.mentalys.app.data.remote.response.mental.test

import com.google.gson.annotations.SerializedName

data class HandwritingTestResponse(
    @SerializedName("status")
    val status: String?,

    @SerializedName("prediction")
    val prediction: Prediction?
)

data class Prediction(

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("result")
    val result: String?,

    @SerializedName("confidence_percentage")
    val confidencePercentage: String?,

    @SerializedName("filename")
    val filename: String?,

)
