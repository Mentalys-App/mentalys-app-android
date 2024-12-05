package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.ClinicEntity
import com.mentalys.app.data.local.room.ClinicDao
import com.mentalys.app.data.remote.retrofit.ClinicApiService
import com.mentalys.app.utils.Resource

class ClinicRepository(
    private val apiService: ClinicApiService,
    private val clinicDao: ClinicDao,
) {

    // Get Nearby Clinic
    fun getNearbyClinic(lat: Number, lng: Number ): LiveData<Resource<List<ClinicEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getNearbyClinic(lat,lng,5000)
            if (response.isSuccessful) {
                val clinic = response.body()?.data
                val clinicEntities = clinic?.map { it.toClinicEntity() }
                if (clinicEntities != null) {
                    clinicDao.clearClinics()
                    clinicDao.insertListClinics(clinicEntities)
                } else {
                    Log.d("ArticleRepository", "No articles found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("ClinicRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            Log.d("ClinicRepository", "Error fetching clinics: ${e.message}", e)
            emit(Resource.Error(e.message.toString()))
        }

        // Fetch data from the local database (Room)
        val localData = clinicDao.getAllClinics().map { clinicList ->
            if (clinicList.isNotEmpty()) {
                Resource.Success(clinicList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }
        emitSource(localData) // Start observing the local data as the source
    }

    fun get4NearbyClinic(lat: Number, lng: Number ): LiveData<Resource<List<ClinicEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getNearbyClinic(lat,lng,5000)
            if (response.isSuccessful) {
                val clinic = response.body()?.data
                val clinicEntities = clinic?.map { it.toClinicEntity() }
                if (clinicEntities != null) {
                    clinicDao.clearClinics()
                    clinicDao.insertListClinics(clinicEntities)
                } else {
                    Log.d("ArticleRepository", "No articles found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("ClinicRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            Log.d("ClinicRepository", "Error fetching clinics: ${e.message}", e)
            emit(Resource.Error(e.message.toString()))
        }
        // Fetch data from the local database (Room)
        val localData = clinicDao.get4Clinics().map { clinicList ->
            if (clinicList.isNotEmpty()) {
                Resource.Success(clinicList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }
        emitSource(localData) // Start observing the local data as the source
    }

    companion object {
        @Volatile
        private var instance: ClinicRepository? = null
        fun getInstance(
            clinicApiService: ClinicApiService,
            clinicDao: ClinicDao
        ): ClinicRepository = instance ?: synchronized(this) {
            instance ?: ClinicRepository(
                clinicApiService,
                clinicDao,
            )
        }.also { instance = it }
    }

}