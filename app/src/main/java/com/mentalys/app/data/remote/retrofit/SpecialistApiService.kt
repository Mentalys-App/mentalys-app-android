package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.specialist.SpecialistDetailsResponse
import retrofit2.Response
import retrofit2.http.GET

interface SpecialistApiService {

    // todo: replace with actual get specialist by id
    @GET("specialists.json")
    suspend fun getSpecialist(): Response<List<SpecialistDetailsResponse>>

    @GET("specialists.json")
    suspend fun getSpecialists(): Response<List<SpecialistDetailsResponse>>

}