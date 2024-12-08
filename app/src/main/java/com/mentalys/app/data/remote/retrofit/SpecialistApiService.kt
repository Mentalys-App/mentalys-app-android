package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.specialist.SpecialistDetailJsonResponse
import com.mentalys.app.data.remote.response.specialist.SpecialistOneResponse
import retrofit2.Response
import retrofit2.http.GET

interface SpecialistApiService {

    @GET("psychiatrists/{id}")
    suspend fun getSpecialist(id: String): Response<SpecialistOneResponse>

    @GET("psychiatrists")
    suspend fun getSpecialists(): Response<SpecialistDetailJsonResponse>

}