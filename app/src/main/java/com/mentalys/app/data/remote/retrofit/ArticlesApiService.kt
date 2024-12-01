package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.article.ArticleListResponse
import com.mentalys.app.data.remote.response.article.ArticleResponse
import com.mentalys.app.data.remote.response.mental_test.HandwritingResponse
import com.mentalys.app.data.remote.response.mental_test.QuizResponse
import com.mentalys.app.data.remote.response.mental_test.VoiceResponse
import com.mentalys.app.data.repository.MentalTestRepository.QuizRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ArticlesApiService {
    @GET("MS2024001")
    suspend fun getArticle(): ArticleResponse

    @GET("articles.json")
    suspend fun getAllArticle(): Response<ArticleListResponse>
}