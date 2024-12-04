package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.data.remote.response.article.ArticleListResponse
import com.mentalys.app.data.remote.response.article.ArticleResponse
import com.mentalys.app.data.remote.response.food.FoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticlesApiService {

    @GET("article")
    suspend fun getAllArticle(): Response<ArticleListResponse>

    @GET("article/{id}")
    suspend fun getArticleById(@Path("id") id: String): Response<ArticleResponse>


    @GET("food")
    suspend fun getAllFood(): Response<FoodResponse>

    @GET("food/{id}")
    suspend fun getFoodById(@Path("id") id: String): Response<FoodResponse>

}