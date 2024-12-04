package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.data.remote.retrofit.MainApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.BuildConfig
import com.mentalys.app.data.remote.request.auth.LoginRequest
import com.mentalys.app.data.remote.request.auth.RegisterRequest
import com.mentalys.app.data.remote.request.auth.ResetPasswordRequest
import com.mentalys.app.data.remote.response.auth.LoginResponse
import com.mentalys.app.data.remote.response.auth.RegisterResponse
import com.mentalys.app.data.remote.response.auth.ResetPasswordResponse
import com.mentalys.app.data.remote.response.profile.ProfileResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainRepository(
    private val mainApiService: MainApiService,
    private val articleDao: ArticleDao,
) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun getMentalHealthTips(prompt: String): LiveData<Resource<String>> = liveData {
        emit(Resource.Loading)
        try {
            val response = generativeModel.generateContent(prompt)
            val resultText = response.text ?: "Something went wrong."
            emit(Resource.Success(resultText))
        } catch (e: Exception) {
            Log.d("ArticleRepository", "getArticle: ${e.message.toString()} ")
            emit(Resource.Error(e.message.toString()))
        }
//        val localData: LiveData<Resource<String>> = Resource.=()
//        emitSource(localData)
    }

    // AUTHENTICATION
    fun registerUser(
        email: String,
        password: String,
        confirmPassword: String
    ): LiveData<Resource<RegisterResponse>> = liveData {
        emit(Resource.Loading)
        val request = RegisterRequest(email, password, confirmPassword)
        try {
            val response = mainApiService.registerUser(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Error("Response body is null"))
                }
            } else {
                // Parse error response into a structured format
                val errorJson = response.errorBody()?.string()
                val errorMessage = try {
                    val errorResponse = Gson().fromJson(errorJson, RegisterResponse::class.java)
                    errorResponse.message
                } catch (e: Exception) {
                    "Unknown error occurred"
                }
                errorMessage?.let { Resource.Error(it) }?.let { emit(it) }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun loginUser(email: String, password: String): LiveData<Resource<LoginResponse>> = liveData {
        emit(Resource.Loading)
        val request = LoginRequest(email, password)
        try {
            val response = mainApiService.loginUser(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Error("Response body is null"))
                }
            } else {
                // Parse error response into a structured format
                val errorJson = response.errorBody()?.string()
                val errorMessage = try {
                    val errorResponse = Gson().fromJson(errorJson, LoginResponse::class.java)
                    errorResponse.message
                } catch (e: Exception) {
                    "Unknown error occurred"
                }
                errorMessage?.let { Resource.Error(it) }?.let { emit(it) }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun resetPassword(email: String): LiveData<Resource<ResetPasswordResponse>> = liveData {
        emit(Resource.Loading)
        val request = ResetPasswordRequest(email)
        try {
            val response = mainApiService.resetPassword(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Error("Response body is null"))
                }
            } else {
                // Parse error response into a structured format
                val errorJson = response.errorBody()?.string()
                val errorMessage = try {
                    val errorResponse =
                        Gson().fromJson(errorJson, ResetPasswordResponse::class.java)
                    errorResponse.message
                } catch (e: Exception) {
                    "Unknown error occurred"
                }
                errorMessage?.let { Resource.Error(it) }?.let { emit(it) }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    // Check have profile or not
    fun getProfile(token: String): LiveData<Resource<ProfileResponse>> = liveData {
        emit(Resource.Loading)
        try {
            val response = mainApiService.getProfile("Bearer $token")
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Resource.Success(responseBody))
                } else {
                    emit(Resource.Error("Response body is null"))
                }
            } else {
                // Handle specific HTTP error codes
                when (response.code()) {
                    404 -> emit(Resource.Error("Profile not found (404)."))
                    else -> emit(Resource.Error("Error ${response.code()}: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Log.d("MainRepository", "getMain: ${e.message.toString()} ")
            emit(Resource.Error(e.message.toString()))
        }
    }

    // Profile Update Method
    fun updateProfile(
        token: String,
        username: String?,
        fullName: String?,
        birthDate: String?,
        location: String?,
        gender: String?,
        profilePicFile: File? // Optional profile picture file
    ): LiveData<Resource<ProfileResponse>> = liveData {
        emit(Resource.Loading)  // Emitting loading state

        // Prepare request body for the text fields only if they are non-null and non-empty
        val usernameRequestBody = username?.takeIf { it.isNotEmpty() }?.let {
            RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        val fullNameRequestBody = fullName?.takeIf { it.isNotEmpty() }?.let {
            RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        val birthDateRequestBody = birthDate?.takeIf { it.isNotEmpty() }?.let {
            RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        val locationRequestBody = location?.takeIf { it.isNotEmpty() }?.let {
            RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        val genderRequestBody = gender?.takeIf { it.isNotEmpty() }?.let {
            RequestBody.create("text/plain".toMediaTypeOrNull(), it)
        }

        // Prepare the profile picture if it exists and is not null
        val profilePicPart = profilePicFile?.takeIf { it.exists() }?.let {
            val profilePicRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), it)
            MultipartBody.Part.createFormData("profile_pic", it.name, profilePicRequestBody)
        }

        try {
            // Call the updateProfile API only with the valid (non-null) parts
            val response = mainApiService.updateProfile(
                "Bearer $token",
                usernameRequestBody,
                fullNameRequestBody,
                birthDateRequestBody,
                locationRequestBody,
                genderRequestBody,
                profilePicPart
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it)) // Emit the success result
                } ?: run {
                    emit(Resource.Error("Empty response body"))
                }
            } else {
                val errorJson = response.errorBody()?.string()
                val errorMessage =
                    "Failed to update profile" // You can parse the error further if needed
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }




    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            mainApiService: MainApiService,
            articleDao: ArticleDao,
        ): MainRepository = instance ?: synchronized(this) {
            instance ?: MainRepository(
                mainApiService,
                articleDao,
            )
        }.also { instance = it }
    }

}