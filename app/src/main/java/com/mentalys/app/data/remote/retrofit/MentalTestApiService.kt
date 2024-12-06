package com.mentalys.app.data.remote.retrofit

import QuizTestResponse
import com.mentalys.app.data.remote.response.mental.test.HandwritingTestResponse
import com.mentalys.app.data.remote.response.mental.test.VoiceTestResponse
import com.mentalys.app.data.repository.MentalTestRepository.QuizRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MentalTestApiService {

    @Multipart
    @POST("ml/handwriting")
    suspend fun testHandwriting(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
    ): HandwritingTestResponse

    @Multipart
    @POST("ml/audio")
    suspend fun testVoice(
        @Header("Authorization") token: String,
        @Part audio: MultipartBody.Part,
    ): VoiceTestResponse

    @POST("ml/quiz")
    suspend fun quizTest(
        @Header("Authorization") token: String,
        @Body body: QuizRequest,
    ): QuizTestResponse

}