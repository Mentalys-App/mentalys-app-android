package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.article.ArticleResponse
import retrofit2.http.GET

interface ApiService {
    @GET("MS2024001")
    suspend fun getArticle(): ArticleResponse
}