package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.specialist.SpecialistDetailsResponse
import retrofit2.Response
import retrofit2.http.GET

interface SpecialistApiService {

    @GET("specialist.json")
    suspend fun getSpecialist(): Response<SpecialistDetailsResponse>

    @GET("specialists.json")
    suspend fun getSpecialists(): Response<List<SpecialistDetailsResponse>>

}