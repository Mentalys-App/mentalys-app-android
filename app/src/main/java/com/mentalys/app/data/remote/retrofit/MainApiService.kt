package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.request.auth.LoginRequest
import com.mentalys.app.data.remote.request.auth.RegisterRequest
import com.mentalys.app.data.remote.response.auth.LoginResponse
import com.mentalys.app.data.remote.response.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApiService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

}