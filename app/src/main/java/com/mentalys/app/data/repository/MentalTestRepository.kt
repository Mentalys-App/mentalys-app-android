package com.mentalys.app.data.repository

import QuizTestResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.room.MentalHistoryDao
import com.mentalys.app.data.remote.response.mental.HistoryItem
import com.mentalys.app.data.remote.response.mental.test.HandwritingTestResponse
import com.mentalys.app.data.remote.response.mental.test.VoiceTestResponse
import com.mentalys.app.data.remote.retrofit.MainApiService
import com.mentalys.app.data.remote.retrofit.MentalTestApiService
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import com.mentalys.app.utils.mapHistoryItems

class MentalTestRepository private constructor(private val apiService: MentalTestApiService) {

    suspend fun testHandwriting(
        token: String,
        photo: MultipartBody.Part,
    ): Resource<HandwritingTestResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.testHandwriting("Bearer $token", photo)
                if (response.status == "success") {
                    Resource.Success(response)
                } else {
                    Resource.Error("Prediction failed")
                }
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Resource.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Resource.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun quizTest(
        token: String, quizRequest: QuizRequest
    ): Resource<QuizTestResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.quizTest(
                    "Bearer $token", quizRequest
                )
                if (response.status == "success") {
                    Resource.Success(response)
                } else {
                    Resource.Error("Prediction failed")
                }
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Resource.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Resource.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun testVoice(
        token: String,
        audio: MultipartBody.Part,
    ): Resource<VoiceTestResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.testVoice("Bearer $token", audio)
                if (response.status == "success") {
                    Resource.Success(response)
                } else {
                    Resource.Error("Prediction failed")
                }
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Resource.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Resource.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    data class QuizRequest(
        val age: String,
        val feeling_nervous: Boolean,
        val panic: Boolean,
        val breathing_rapidly: Boolean,
        val sweating: Boolean,
        val trouble_in_concentration: Boolean,
        val having_trouble_in_sleeping: Boolean,
        val having_trouble_with_work: Boolean,
        val hopelessness: Boolean,
        val anger: Boolean,
        val over_react: Boolean,
        val change_in_eating: Boolean,
        val suicidal_thought: Boolean,
        val feeling_tired: Boolean,
        val close_friend: Boolean,
        val social_media_addiction: Boolean,
        val weight_gain: Boolean,
        val introvert: Boolean,
        val popping_up_stressful_memory: Boolean,
        val having_nightmares: Boolean,
        val avoids_people_or_activities: Boolean,
        val feeling_negative: Boolean,
        val trouble_concentrating: Boolean,
        val blaming_yourself: Boolean,
        val hallucinations: Boolean,
        val repetitive_behaviour: Boolean,
        val seasonally: Boolean,
        val increased_energy: Boolean
    )

    companion object {
        @Volatile
        private var INSTANCE: MentalTestRepository? = null

        fun getInstance(
            apiService: MentalTestApiService
        ): MentalTestRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MentalTestRepository(apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}