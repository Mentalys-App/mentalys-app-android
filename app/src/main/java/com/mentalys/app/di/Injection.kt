package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.ArticleRepository
import com.mentalys.app.data.local.room.ArticleDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): ArticleRepository {
        val apiService = ApiConfig.getApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.articleDao()
        return ArticleRepository.getInstance(apiService, dao)
    }
}