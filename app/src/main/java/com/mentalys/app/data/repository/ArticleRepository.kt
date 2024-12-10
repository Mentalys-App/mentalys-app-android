package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.utils.Resource
import com.mentalys.app.data.remote.retrofit.ArticlesApiService
import com.mentalys.app.utils.getErrorMessage

class ArticleRepository(
    private val apiService: ArticlesApiService,
    private val articleDao: ArticleDao,
) {

    fun getArticle(id: String): LiveData<Resource<ArticleEntity>> = liveData {
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
            val errorMessage = getErrorMessage(e)
            Log.d("ArticleRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }


        // Fetch data from the local database (Room)
        val localData = articleDao.getArticle(id).map { article ->
            if (article != null) {
                Resource.Success(article) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

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
            val errorMessage = getErrorMessage(e)
            Log.d("ArticleRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = articleDao.getListArticle().map { articleList ->
            if (articleList.isNotEmpty()) {
                Resource.Success(articleList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData)

    }

    fun get3Article(): LiveData<Resource<List<ArticleListEntity>>> = liveData {
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

        // Fetch data from the local database (Room)
        val localData = articleDao.get3ListArticle().map { articleList ->
            if (articleList.isNotEmpty()) {
                Resource.Success(articleList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData)

    }

    // Get Food
    fun getAllFood(): LiveData<Resource<List<FoodEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getAllFood()
            if (response.isSuccessful) {
                val foods = response.body()?.data
                val foodEntity = foods?.map { it.toEntity() }
                if (foodEntity != null) {
                    articleDao.insertFood(foodEntity)
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
            val errorMessage = getErrorMessage(e)
            Log.d("ArticleRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = articleDao.getFood().map { foodList ->
            if (foodList.isNotEmpty()) {
                Resource.Success(foodList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData) // Start observing the local data as the source

    }

    // Get Food
    fun get4Food(): LiveData<Resource<List<FoodEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getAllFood()
            if (response.isSuccessful) {
                val foods = response.body()?.data
                val foodEntity = foods?.map { it.toEntity() }
                if (foodEntity != null) {
                    articleDao.insertFood(foodEntity)
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

        // Fetch data from the local database (Room)
        val localData = articleDao.get4Food().map { foodList ->
            if (foodList.isNotEmpty()) {
                Resource.Success(foodList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

        emitSource(localData) // Start observing the local data as the source

    }

    fun getMentalStateArticle(mentalState: String): LiveData<Resource<List<ArticleListEntity>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getMentalStateArticle(mentalState)
            if (response.isSuccessful) {
                val articles = response.body()?.data?.articles
                val articleEntities = articles?.map { it.toEntity() }
                if (articleEntities != null) {
                    articleDao.clearArticles()
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
            val errorMessage = getErrorMessage(e)
            Log.d("ArticleRepository", "Error: $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }

        // Fetch data from the local database (Room)
        val localData = articleDao.getListArticle().map { articleList ->
            if (articleList.isNotEmpty()) {
                Resource.Success(articleList) // Emit data only if not empty
            } else {
                Resource.Error("No local data available.") // Emit error if database is empty
            }
        }

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