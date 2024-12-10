package com.mentalys.app.data.remote.retrofit

import androidx.room.Dao
import com.mentalys.app.data.remote.response.music.MusicDetailResponse
import com.mentalys.app.data.remote.response.music.MusicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApiService {
    @GET("search/text")
    suspend fun searchSounds(
        @Header("Authorization") authorization: String,
        @Query("query") query: String
    ): Response<MusicResponse>

    @GET("sounds/{id}")
    suspend fun searchSound(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<MusicDetailResponse>
}