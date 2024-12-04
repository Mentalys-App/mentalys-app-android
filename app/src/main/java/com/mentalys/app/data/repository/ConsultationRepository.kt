package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.ConsultationEntity
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.data.remote.retrofit.ConsultationApiService
import com.mentalys.app.data.remote.retrofit.MainApiService
import com.mentalys.app.utils.Resource

class ConsultationRepository(
    private val apiService: ConsultationApiService,
    private val articleDao: ArticleDao,
) {

    fun getConsultations(): LiveData<Resource<List<ConsultationEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getConsultations()
            if (response.isSuccessful) {
                val consultations = response.body()?.map { it.toEntity() }
                if (consultations != null) {
                    articleDao.insertConsultation(consultations)
                } else {
                    Log.d("ConsultationRepository", "No specialist found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("ConsultationRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            Log.d("ConsultationRepository", "Error fetching articles: ${e.message}", e)
            emit(Resource.Error(e.message.toString()))
        }

        // Fetch data from the local database (Room)
        val localData = articleDao.getConsultation().map { consultations ->
            if (consultations.isNotEmpty()) {
                Resource.Success(consultations) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData) // Start observing the local data as the source
    }


    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            mainApiService: MainApiService,
            articleDao: ArticleDao,
        ): MainRepository = instance ?: synchronized(this) {
            instance ?: MainRepository(
                mainApiService,
                articleDao,
            )
        }.also { instance = it }
    }


}