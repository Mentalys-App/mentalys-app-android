package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.request.auth.LoginRequest
import com.mentalys.app.data.remote.request.auth.RegisterRequest
import com.mentalys.app.data.remote.request.auth.ResetPasswordRequest
import com.mentalys.app.data.remote.response.auth.LoginResponse
import com.mentalys.app.data.remote.response.auth.RegisterResponse
import com.mentalys.app.data.remote.response.auth.ResetPasswordResponse
import com.mentalys.app.data.remote.response.mental_test.HandwritingResponse
import com.mentalys.app.data.remote.response.mental_test.HistoryResponse
import com.mentalys.app.data.remote.response.mental_test.QuizResponse
import com.mentalys.app.data.remote.response.mental_test.VoiceResponse
import com.mentalys.app.data.repository.MentalTestRepository.QuizRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MainApiService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    @Multipart
    @POST("ml/handwriting")
    suspend fun testHandwriting(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): HandwritingResponse

    @Multipart
    @POST("ml/audio")
    suspend fun testVoice(
        @Header("Authorization") token: String,
        @Part audio: MultipartBody.Part,
    ): VoiceResponse

    @POST("ml/quiz")
    suspend fun quizTest(
        @Header("Authorization") token: String,
        @Body body: QuizRequest,
    ): QuizResponse

    @GET("ml/all-history")
    suspend fun getAllHistory(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("sortBy") sortBy: String = "timestamp",
        @Query("sortOrder") sortOrder: String = "desc"
    ): HistoryResponse
}