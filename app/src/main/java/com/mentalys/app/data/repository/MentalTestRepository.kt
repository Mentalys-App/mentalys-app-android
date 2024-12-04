package com.mentalys.app.data.repository

import QuizTestResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.HandwritingEntity
import com.mentalys.app.data.local.room.HandwritingDao
import com.mentalys.app.data.remote.response.mental.history.HandwritingResponse
import com.mentalys.app.data.remote.response.mental.history.HistoryItem
import com.mentalys.app.data.remote.response.mental.history.QuizResponse
import com.mentalys.app.data.remote.response.mental.history.VoiceResponse
import com.mentalys.app.data.remote.response.mental.test.HandwritingTestResponse
import com.mentalys.app.data.remote.response.mental.test.VoiceTestResponse
import com.mentalys.app.data.remote.retrofit.MainApiService
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import com.mentalys.app.utils.mapHistoryItems

class MentalTestRepository private constructor(
    private val apiService: MainApiService,
    private val dao: HandwritingDao
) {

    fun getHandwritingHistory(token: String): LiveData<Resource<List<HandwritingEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getHandwritingHistory("Bearer $token")
            if (response.isSuccessful) {
                val handwriting = response.body()?.history?.map { it.toEntity() }
                 if (handwriting != null) {
                    dao.insertHandwritingHistory(handwriting)
                 } else {
                    Log.d("MentalTestRepository", "No handwriting history found in response.")
                 }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("MentalTestRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            Log.d("MentalTestRepository", "Error fetching histories: ${e.message}", e)
            emit(Resource.Error(e.message.toString()))
        }

        // Fetch data from the local database (Room)
        val localData = dao.getHandwritingHistory().map { handwriting ->
            if (handwriting != null) {
                Resource.Success(handwriting) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData) // Start observing the local data as the source
    }



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

    suspend fun getAllHistory(
        token: String,
        page: Int = 1,
        limit: Int = 10,
        startDate: String? = null,
        endDate: String? = null,
        sortBy: String = "timestamp",
        sortOrder: String = "desc"
    ): Resource<List<HistoryItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllHistory(
                    "Bearer $token", page, limit, startDate, endDate, sortBy, sortOrder
                )
                val mappedHistory = mapHistoryItems(response.history)
                Resource.Success(mappedHistory)
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Resource.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Resource.Error("An unexpected error occurred: ${e.message}")}
        }
    }

//    suspend fun getHandwritingHistory(
//        token: String,
//        type: String,
//        page: Int = 1,
//        limit: Int = 10,
//        startDate: String? = null,
//        endDate: String? = null,
//        sortBy: String = "timestamp",
//        sortOrder: String = "desc"
//    ): Resource<List<HandwritingHistoryItem>> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = apiService.getHandwritingHistory(
//                    "Bearer $token", type, page, limit, startDate, endDate, sortBy, sortOrder
//                )
//                Resource.Success(response.handwritingHistory)
//            } catch (e: IOException) {
//                Resource.Error("Network error: ${e.message}")
//            } catch (e: HttpException) {
//                Resource.Error("HTTP error: ${e.message}")
//            } catch (e: Exception) {
//                Resource.Error("An unexpected error occurred: ${e.message}")}
//        }
//    }

//    @OptIn(ExperimentalPagingApi::class)
//    fun getHandwritingTests(
//        token: String,
//        startDate: String? ,
//        endDate: String?,
//        sortBy: String = "timestamp",
//        sortOrder: String = "desc"
//    ) = Pager(
//        config = PagingConfig(
//            pageSize = 10,
//            enablePlaceholders = false,
//        ),
//        remoteMediator = HandwritingTestRemoteMediator(
//            apiService = apiService,
//            database = database,
//            token = token,
//            startDate = startDate,
//            endDate = endDate,
//            sortBy = sortBy,
//            sortOrder = sortOrder
//        ),
//        pagingSourceFactory = { database.handwritingTestDao().getAllHandwritingTests() }
//    ).flow



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
            apiService: MainApiService,
            database: HandwritingDao
        ): MentalTestRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MentalTestRepository(apiService,database)
                INSTANCE = instance
                instance
            }
        }
    }
}