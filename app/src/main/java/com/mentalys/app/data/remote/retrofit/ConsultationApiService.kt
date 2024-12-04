package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.consultation.ConsultationResponse
import retrofit2.Response
import retrofit2.http.GET

interface ConsultationApiService {
    @GET("...")
    suspend fun getConsultations(): Response<List<ConsultationResponse>>
}