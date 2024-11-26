package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.ArticlesRepository
import com.mentalys.app.data.MainRepository
import com.mentalys.app.data.local.room.ArticleDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig

object Injection {
    fun provideMainRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getMainApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.articleDao()
        return MainRepository.getInstance(apiService, dao)
    }
    fun provideArticlesRepository(context: Context): ArticlesRepository {
        val apiService = ApiConfig.getArticlesApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.articleDao()
        return ArticlesRepository.getInstance(apiService, dao)
    }
}