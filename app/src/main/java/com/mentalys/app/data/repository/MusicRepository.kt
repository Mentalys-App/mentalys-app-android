package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.BuildConfig
import com.mentalys.app.data.local.entity.MusicDetailEntity
import com.mentalys.app.data.local.entity.MusicItemEntity
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.data.local.room.MusicDao
import com.mentalys.app.data.local.room.SpecialistDao
import com.mentalys.app.data.remote.retrofit.MusicApiService
import com.mentalys.app.data.remote.retrofit.SpecialistApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.getErrorMessage

class MusicRepository(
    private val apiService: MusicApiService,
    private val dao: MusicDao,
) {

    fun getMusics(query: String): LiveData<Resource<List<MusicItemEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.searchSounds(BuildConfig.FREE_SOUND_API_KEY, query)
            if (response.isSuccessful) {
                val specialists = response.body()?.results?.map { it.toEntity() }
                if (specialists != null) {
                    dao.insertMusics(specialists)
                } else {
                    Log.d("MusicRepository", "No specialist found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("MusicRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            val errorMessage = getErrorMessage(e)
            Log.d("MusicRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = dao.getMusics().map { specialists ->
            if (specialists.isNotEmpty()) {
                Resource.Success(specialists) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData)

    }

    fun searchMusic(id: Int): LiveData<Resource<MusicDetailEntity>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.searchSound(BuildConfig.FREE_SOUND_API_KEY, id)
            if (response.isSuccessful) {
                val music = response.body()?.toEntity()
                if (music != null) {
                     dao.insertMusic(music)
                } else {
                    Log.d("MusicRepository", "No music found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("MusicRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            val errorMessage = getErrorMessage(e)
            Log.d("MusicRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = dao.getMusic(id).map { specialists ->
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
        private var instance: MusicRepository? = null
        fun getInstance(
            apiService: MusicApiService,
            dao: MusicDao,
        ): MusicRepository = instance ?: synchronized(this) {
            instance ?: MusicRepository(
                apiService,
                dao,
            )
        }.also { instance = it }
    }

}