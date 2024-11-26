package com.mentalys.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.data.remote.retrofit.MainApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.BuildConfig
import com.mentalys.app.data.remote.request.auth.RegisterRequest
import com.mentalys.app.data.remote.response.auth.RegisterResponse

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
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.message ?: "An unexpected error occurred"
                )
            ) // Emit exception as error
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