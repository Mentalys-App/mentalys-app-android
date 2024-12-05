package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.local.room.ArticleDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig
import com.mentalys.app.data.repository.ArticleRepository
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

    fun provideMentalTestRepository(context: Context): MentalTestRepository {
        val apiService = ApiConfig.getMainApiService()
        val database = ArticleDatabase.getInstance(context)
        val dao = database.handwritingDao()
        val voiceDao = database.voiceDao()
        val quizDao = database.quizDao()
        return MentalTestRepository.getInstance(apiService, dao,voiceDao, quizDao)
    }

}