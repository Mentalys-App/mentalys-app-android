package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.request.auth.LoginRequest
import com.mentalys.app.data.remote.request.auth.RegisterRequest
import com.mentalys.app.data.remote.request.auth.ResetPasswordRequest
import com.mentalys.app.data.remote.request.payment.PaymentChargeRequest
import com.mentalys.app.data.remote.response.auth.LoginResponse
import com.mentalys.app.data.remote.response.auth.RegisterResponse
import com.mentalys.app.data.remote.response.auth.ResetPasswordResponse
import com.mentalys.app.data.remote.response.payment.PaymentChargeResponse
import com.mentalys.app.data.remote.response.profile.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface MainApiService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): Response<ProfileResponse>

    @Multipart
    @PUT("user/update")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody?,
        @Part("full_name") fullName: RequestBody?,
        @Part("birth_date") birthDate: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part profilePic: MultipartBody.Part? // Optional profile picture
    ): Response<ProfileResponse>

    @POST("midtrans/charge")
    suspend fun paymentCharge(
        @Header("Authorization") token: String,
        @Body paymentCharge: PaymentChargeRequest
    ): Response<PaymentChargeResponse>

}