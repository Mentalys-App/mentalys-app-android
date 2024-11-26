package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.article.ArticleResponse
import retrofit2.http.GET

interface ArticlesApiService {
    @GET("MS2024001")
    suspend fun getArticle(): ArticleResponse
}