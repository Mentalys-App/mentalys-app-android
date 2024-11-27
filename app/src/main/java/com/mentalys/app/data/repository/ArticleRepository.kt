package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.ai.client.generativeai.GenerativeModel
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.data.remote.retrofit.ApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.mapAuthorToEntity
import com.mentalys.app.utils.mapContentListToEntity
import com.mentalys.app.utils.mapMetadataToEntity
import com.mentalys.app.BuildConfig

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