package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.article.ArticleListResponse
import retrofit2.Response
import retrofit2.http.GET

interface ArticlesApiService {
    @GET("article")
    suspend fun getAllArticle(): Response<ArticleListResponse>
}