package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryEntity
import com.mentalys.app.data.local.room.MentalHistoryDao
import com.mentalys.app.data.remote.retrofit.MentalHistoryApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.getErrorMessage

class MentalHistoryRepository private constructor(
    private val apiService: MentalHistoryApiService,
    private val dao: MentalHistoryDao
) {

    fun getHandwritingHistory(token: String): LiveData<Resource<List<HandwritingHistoryEntity>>> =
        liveData {
            emit(Resource.Loading)
            try {
                val response = apiService.getHandwritingHistory("Bearer $token")
                if (response.isSuccessful) {
                    val handwriting = response.body()?.history?.map { it.toEntity() }
                    if (handwriting != null) {
                        dao.insertHandwritingHistory(handwriting)
                    } else {
                        Log.d(
                            "MentalHistoryRepository",
                            "No handwriting history found in response."
                        )
                    }
                } else {
                    // Handle the case when the response is not successful
                    val errorMessage = response.message() ?: "Unknown error"
                    Log.d("MentalHistoryRepository", "API call failed: $errorMessage")
                    emit(Resource.Error(errorMessage))  // Emit error state with the response error message
                }
            } catch (e: Exception) {
                val errorMessage = getErrorMessage(e)
                Log.d("MentalHistoryRepository", "Error: $errorMessage", e)
                emit(Resource.Error(errorMessage))
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


    fun getQuizHistory(token: String): LiveData<Resource<List<QuizHistoryEntity>>> =
        liveData {
            emit(Resource.Loading)
            try {
                val response = apiService.getQuizHistory("Bearer $token")
                if (response.isSuccessful) {
                    val quiz = response.body()?.history?.map { it.toEntity() }
                    if (quiz != null) {
                        dao.insertQuizHistory(quiz)
                    } else {
                        Log.d("MentalHistoryRepository", "No quiz history found in response.")
                    }
                } else {
                    // Handle the case when the response is not successful
                    val errorMessage = response.message() ?: "Unknown error"
                    Log.d("MentalHistoryRepository", "API call failed: $errorMessage")
                    emit(Resource.Error(errorMessage))  // Emit error state with the response error message
                }
            } catch (e: Exception) {
                val errorMessage = getErrorMessage(e)
                Log.d("MentalHistoryRepository", "Error: $errorMessage", e)
                emit(Resource.Error(errorMessage))
            }

            // Fetch data from the local database (Room)
            val localData = dao.getQuizHistory().map { quiz ->
                if (quiz != null) {
                    Resource.Success(quiz) // Emit data only if not empty
                } else {
                    Resource.Error("No local data available.") // Emit error if database is empty
                }
            }

            emitSource(localData) // Start observing the local data as the source
        }

    fun getVoiceHistory(token: String): LiveData<Resource<List<VoiceHistoryEntity>>> =
        liveData {
            emit(Resource.Loading)
            try {
                val response = apiService.getVoiceHistory("Bearer $token")
                if (response.isSuccessful) {
                    val voice = response.body()?.history?.map { it.toEntity() }
                    if (voice != null) {
                        dao.insertVoiceHistory(voice)
                    } else {
                        Log.d("MentalHistoryRepository", "No voice history found in response.")
                    }
                } else {
                    // Handle the case when the response is not successful
                    val errorMessage = response.message() ?: "Unknown error"
                    Log.d("MentalHistoryRepository", "API call failed: $errorMessage")
                    emit(Resource.Error(errorMessage))  // Emit error state with the response error message
                }
            } catch (e: Exception) {
                val errorMessage = getErrorMessage(e)
                Log.d("MentalHistoryRepository", "Error: $errorMessage", e)
                emit(Resource.Error(errorMessage))
            }

            // Fetch data from the local database (Room)
            val localData = dao.getVoiceHistory().map { voice ->
                if (voice != null) {
                    Resource.Success(voice) // Emit data only if not empty
                } else {
                    Resource.Error("No local data available.") // Emit error if database is empty
                }
            }

            emitSource(localData) // Start observing the local data as the source
        }


    companion object {
        @Volatile
        private var INSTANCE: MentalHistoryRepository? = null

        fun getInstance(
            apiService: MentalHistoryApiService,
            dao: MentalHistoryDao
        ): MentalHistoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MentalHistoryRepository(apiService, dao)
                INSTANCE = instance
                instance
            }
        }
    }
}