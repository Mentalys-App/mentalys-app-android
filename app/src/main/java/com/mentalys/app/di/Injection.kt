package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.local.room.ArticleDatabase
import com.mentalys.app.data.local.room.ClinicDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.data.repository.ClinicRepository
import com.mentalys.app.data.repository.MentalTestRepository

object Injection {
    fun provideMainRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getMainApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.articleDao()
        return MainRepository.getInstance(apiService, dao)
    }
    fun provideArticlesRepository(context: Context): ArticleRepository {
        val apiService = ApiConfig.getArticlesApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.articleDao()
        return ArticleRepository.getInstance(apiService, dao)
    }
    fun provideClinicsRepository(context: Context): ClinicRepository {
        val apiService = ApiConfig.getClinicApiService()
        val database = ClinicDatabase.getInstance(context)
        val dao = database.clinicDao()
        return ClinicRepository.getInstance(apiService, dao)
    }

    fun provideMentalTestRepository(): MentalTestRepository {
        val apiService = ApiConfig.getMainApiService()
        return MentalTestRepository.getInstance(apiService)
    }
}