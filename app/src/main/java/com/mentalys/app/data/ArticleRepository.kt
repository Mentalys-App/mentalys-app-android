package com.mentalys.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.ai.client.generativeai.GenerativeModel
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.room.ArticleDao
//import com.mentalys.app.data.remote.response.article.ArticlesResponse
import com.mentalys.app.data.remote.retrofit.ApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.mapAuthorToEntity
import com.mentalys.app.utils.mapContentListToEntity
import com.mentalys.app.utils.mapMetadataToEntity
import com.mentalys.app.BuildConfig
//import com.mentalys.app.data.local.entity.GeminiEntity

class ArticleRepository(
    private val apiService: ApiService,
    private val articleDao: ArticleDao,
) {

    fun getArticle(): LiveData<Resource<List<ArticleEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getArticle()
            val articles =
                ArticleEntity(
                    id = response.article.id,
                    title = response.article.title,
                    authorEntity = mapAuthorToEntity(response.article.author),
                    metadataEntity = mapMetadataToEntity(response.article.metadata),
                    contentEntity = mapContentListToEntity(response.article.content)
                )
            articleDao.insertArticle(articles)
        } catch (e: Exception) {
            Log.d("ArticleRepository", "getArticle: ${e.message.toString()} ")
            emit(Resource.Error(e.message.toString()))
        }

        // Save to room
        val localData: LiveData<Resource<List<ArticleEntity>>> = articleDao.getAllArticles().map {
            Resource.Success(it)
        }
        emitSource(localData)
    }

//    fun getArticles(): LiveData<PagingData<ArticlesResponse>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            pagingSourceFactory = {
//                ArticlePagingSource(apiService)
//            }
//        ).liveData
//    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

//    fun getMentalHealthTips(prompt: String): LiveData<Resource<List<GeminiEntity>>> = liveData {
//        emit(Resource.Loading)
//        try {
//            val response = generativeModel.generateContent(prompt)
//            val tips = response.text ?: "Something went wrong."
//            articleDao.insertTips(GeminiEntity(text = tips))
//        } catch (e: Exception) {
//            Log.d("ArticleRepository", "getMentalHealthTips: ${e.message.toString()} ")
//            emit(Resource.Error(e.message.toString()))
//        }
//
//        val localData: LiveData<Resource<List<GeminiEntity>>> = articleDao.getAllTips().map {
//            Resource.Success(it)
//        }
//        emitSource(localData)
//    }

//    suspend fun getMentalHealthTips(prompt: String): String {
//        return try {
//            val response = generativeModel.generateContent(prompt)
//            response.text ?: "Something went wrong."
//        } catch (e: Exception) {
//            "An error occurred"
//        }
//    }

    fun getMentalHealthTips(prompt: String): LiveData<Resource<String>> = liveData {
        emit(Resource.Loading)
        try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "Something went wrong."
        } catch (e: Exception) {
            Log.d("ArticleRepository", "getArticle: ${e.message.toString()} ")
            emit(Resource.Error(e.message.toString()))
        }
//        val localData: LiveData<Resource<String>> = Resource.=()
//        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance(
            apiService: ApiService,
            articleDao: ArticleDao,
        ): ArticleRepository = instance ?: synchronized(this) {
            instance ?: ArticleRepository(
                apiService,
                articleDao,
            )
        }.also { instance = it }
    }

}