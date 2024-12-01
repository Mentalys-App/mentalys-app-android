package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.utils.Resource
import com.mentalys.app.data.remote.retrofit.ArticlesApiService

class ArticleRepository(
    private val apiService: ArticlesApiService,
    private val articleDao: ArticleDao,
) {

    fun getArticle(id: String): LiveData<Resource<List<ArticleEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getArticleById(id)
            if (response.isSuccessful) {
                val article = response.body()?.data
                val articleEntities = article?.toEntity()
                if (articleEntities != null) {
                    articleDao.insertArticle(articleEntities)
                } else {
                    Log.d("ArticleRepository", "No article found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("ArticleRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            Log.d("ArticleRepository", "Error fetching article: ${e.message}", e)
            emit(Resource.Error(e.message.toString()))
        }

        // Fetch data from the local database (Room) and emit it as LiveData
        val localData: LiveData<Resource<List<ArticleEntity>>> =
            articleDao.getArticle(id).map { Resource.Success(it) }
        emitSource(localData)
    }

    // new
    fun getAllArticle(): LiveData<Resource<List<ArticleListEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getAllArticle()
            if (response.isSuccessful) {
                val articles = response.body()?.data?.articles
                val articleEntities = articles?.map { it.toEntity() }
                if (articleEntities != null) {
                    articleDao.insertListArticle(articleEntities)
                } else {
                    Log.d("ArticleRepository", "No articles found in response.")
                }
            } else {
                // Handle the case when the response is not successful
                val errorMessage = response.message() ?: "Unknown error"
                Log.d("ArticleRepository", "API call failed: $errorMessage")
                emit(Resource.Error(errorMessage))  // Emit error state with the response error message
            }
        } catch (e: Exception) {
            Log.d("ArticleRepository", "Error fetching articles: ${e.message}", e)
            emit(Resource.Error(e.message.toString()))
        }

        // Fetch data from the local database (Room) and emit it as LiveData
        val localData: LiveData<Resource<List<ArticleListEntity>>> =
            articleDao.getListArticle().map { Resource.Success(it) }
        emitSource(localData)
    }


    companion object {
        @Volatile
        private var instance: ArticleRepository? = null
        fun getInstance(
            articleApiService: ArticlesApiService,
            articleDao: ArticleDao,
        ): ArticleRepository = instance ?: synchronized(this) {
            instance ?: ArticleRepository(
                articleApiService,
                articleDao,
            )
        }.also { instance = it }
    }

}