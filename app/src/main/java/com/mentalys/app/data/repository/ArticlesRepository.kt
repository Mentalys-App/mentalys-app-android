package com.mentalys.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.data.remote.response.article.ArticleListData
import com.mentalys.app.data.remote.response.article.ArticleListItem
import com.mentalys.app.data.remote.response.article.ArticleListResponse
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.mapAuthorToEntity
import com.mentalys.app.utils.mapContentListToEntity
import com.mentalys.app.utils.mapMetadataToEntity
import com.mentalys.app.data.remote.retrofit.ArticlesApiService
import com.mentalys.app.data.remote.retrofit.MainApiService

class ArticlesRepository(
    private val apiService: ArticlesApiService,
    private val articleDao: ArticleDao,
) {

//    fun getArticle(): LiveData<Resource<List<ArticleEntity>>> = liveData {
//        emit(Resource.Loading)
//        try {
//            val response = articleApiService.getArticle()
//            val articles =
//                ArticleEntity(
//                    id = response.article.id,
//                    title = response.article.title,
//                    authorEntity = mapAuthorToEntity(response.article.author),
//                    metadataEntity = mapMetadataToEntity(response.article.metadata),
//                    contentEntity = mapContentListToEntity(response.article.content)
//                )
//            articleDao.insertArticle(articles)
//        } catch (e: Exception) {
//            Log.d("ArticleRepository", "getArticle: ${e.message.toString()} ")
//            emit(Resource.Error(e.message.toString()))
//        }
//
//        // Save to room
//        val localData: LiveData<Resource<List<ArticleEntity>>> = articleDao.getAllArticles().map {
//            Resource.Success(it)
//        }
//        emitSource(localData)
//    }

    // new
    fun getAllArticle(): LiveData<Resource<List<ArticleListItem>>> = liveData {
        emit(Resource.Loading)
        try {
            val response = apiService.getAllArticle()
            val articles = response.body()?.data?.articles
            val articleList = articles?.map { article ->
                ArticleListItem(
                    id = article.id,
                    title = article.title,
                    metadata = article.metadata,
                )
            } ?: emptyList() // Default to an empty list if null
            articleDao.insertListArticle(articleList)
        } catch (e: Exception) {
            Log.d("ArticleRepository", "getArticle: ${e.message.toString()} ")
            emit(Resource.Error(e.message.toString()))
        }

        // Save to room
        val localData: LiveData<Resource<List<ArticleListItem>>> =
            articleDao.getAllListArticle().map { Resource.Success(it)
            }
        emitSource(localData)
    }


    companion object {
        @Volatile
        private var instance: ArticlesRepository? = null
        fun getInstance(
            articleApiService: ArticlesApiService,
            articleDao: ArticleDao,
        ): ArticlesRepository = instance ?: synchronized(this) {
            instance ?: ArticlesRepository(
                articleApiService,
                articleDao,
            )
        }.also { instance = it }
    }

}