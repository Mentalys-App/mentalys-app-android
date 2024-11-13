package com.mentalys.app.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.room.ArticleDao
import com.mentalys.app.data.remote.retrofit.ApiService
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.mapAuthorToEntity
import com.mentalys.app.utils.mapContentListToEntity
import com.mentalys.app.utils.mapMetadataToEntity

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