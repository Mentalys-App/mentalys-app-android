package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.clinic.ClinicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClinicApiService {
    @GET("places/search")
    suspend fun getNearbyClinic(
        @Query("lat") lat : Number,
        @Query("lng") lng : Number,
        @Query("radius") radius : Int,
    ) : Response<ClinicResponse>
}