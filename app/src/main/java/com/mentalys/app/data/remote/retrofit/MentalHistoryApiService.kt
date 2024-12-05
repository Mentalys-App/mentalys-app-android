package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.mental.history.HandwritingResponse
import com.mentalys.app.data.remote.response.mental.history.QuizHistoryResponse
import com.mentalys.app.data.remote.response.mental.history.VoiceHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MentalHistoryApiService {

    @GET("ml/history")
    suspend fun getHandwritingHistory(
        @Header("Authorization") token: String,
        @Query("type") type: String = "handwriting_requests",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("sortBy") sortBy: String = "timestamp",
        @Query("sortOrder") sortOrder: String = "desc"
    ): Response<HandwritingResponse>

    @GET("ml/history")
    suspend fun getQuizHistory(
        @Header("Authorization") token: String,
        @Query("type") type: String = "quiz_requests",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("sortBy") sortBy: String = "timestamp",
        @Query("sortOrder") sortOrder: String = "desc"
    ): Response<QuizHistoryResponse>

    @GET("ml/history")
    suspend fun getVoiceHistory(
        @Header("Authorization") token: String,
        @Query("type") type: String = "audio_requests",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("sortBy") sortBy: String = "timestamp",
        @Query("sortOrder") sortOrder: String = "desc"
    ): Response<VoiceHistoryResponse>

}