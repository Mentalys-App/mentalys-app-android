package com.mentalys.app.data.remote.response.mental.history

data class HistoryResponse(
    val status: String?,
    val history: List<HistoryItem>
)

data class HistoryItem(
    val id: String,
    val type: String,
    val prediction: PredictionResponse,
    val timestamp: String
)

data class PredictionResponse(
    val result: Any?
)

data class AudioResult(
    val confidence_scores: Map<String, Double>?,
    val predicted_emotion: String?,
    val support_percentage: Double?,
    val category: String?
)

data class HandwritingResult(
    val confidence_percentage: String?,
    val result: String?,
    val status: String?,
    val confidence: Double?,
    val filename: String?
)

data class QuizResult(
    val diagnosis: String?,
    val confidence_score: Double?,
    val message: String?
)
