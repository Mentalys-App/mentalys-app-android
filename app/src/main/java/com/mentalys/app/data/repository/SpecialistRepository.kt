package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.data.local.room.SpecialistDao
import com.mentalys.app.data.remote.retrofit.SpecialistApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.getErrorMessage

class SpecialistRepository(
    private val apiService: SpecialistApiService,
    private val dao: SpecialistDao,
) {

    fun getConsultations(): LiveData<Resource<List<SpecialistEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getSpecialists()
            if (response.isSuccessful) {
                val specialists = response.body()?.data?.map { it.toEntity() }
                if (specialists != null) {
                    dao.insertSpecialists(specialists)
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
            val errorMessage = getErrorMessage(e)
            Log.d("ConsultationRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = dao.getSpecialists().map { specialists ->
            if (specialists.isNotEmpty()) {
                Resource.Success(specialists) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData) // Start observing the local data as the source
    }

    fun getConsultation(id: String): LiveData<Resource<SpecialistEntity>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getSpecialist(id)
            if (response.isSuccessful) {
                val specialists = response.body()?.data?.toEntity()
                if (specialists != null) {
                    // dao.insertSpecialist(specialists)
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
            val errorMessage = getErrorMessage(e)
            Log.d("ConsultationRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = dao.getSpecialist(id).map { specialists ->
            if (specialists != null) {
                Resource.Success(specialists) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData) // Start observing the local data as the source
    }


    companion object {
        @Volatile
        private var instance: SpecialistRepository? = null
        fun getInstance(
            apiService: SpecialistApiService,
            dao: SpecialistDao,
        ): SpecialistRepository = instance ?: synchronized(this) {
            instance ?: SpecialistRepository(
                apiService,
                dao,
            )
        }.also { instance = it }
    }


}