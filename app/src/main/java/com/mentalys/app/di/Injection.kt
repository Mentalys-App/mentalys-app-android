package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.data.local.room.ArticleDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig
import com.mentalys.app.data.remote.retrofit.ApiConfigMentalTest
import com.mentalys.app.data.repository.MentalTestRepository

object Injection {
    fun provideRepository(context: Context): ArticleRepository {
        val apiService = ApiConfig.getApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.articleDao()
        return ArticleRepository.getInstance(apiService, dao)
    }

    fun mentalTestRepository(): MentalTestRepository {
        val apiService = ApiConfigMentalTest.getApiService()
        return MentalTestRepository.getInstance(apiService)
    }
}