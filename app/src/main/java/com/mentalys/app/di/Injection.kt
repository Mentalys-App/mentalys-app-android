package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.local.room.MainDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.data.repository.MentalHistoryRepository
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.data.repository.SpecialistRepository

object Injection {

    fun provideMainRepository(context: Context): MainRepository {
        val apiService = ApiConfig.getMainApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.articleDao()
        return MainRepository.getInstance(apiService, dao)
    }

    fun provideArticlesRepository(context: Context): ArticleRepository {
        val apiService = ApiConfig.getArticlesApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.articleDao()
        return ArticleRepository.getInstance(apiService, dao)
    }

    fun provideMentalTestRepository(context: Context): MentalTestRepository {
        val apiService = ApiConfig.getMainApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.mentalHistoryDao()
        return MentalTestRepository.getInstance(apiService, dao)
        val dao = database.handwritingDao()
        val voiceDao = database.voiceDao()
        val quizDao = database.quizDao()
        return MentalTestRepository.getInstance(apiService, dao,voiceDao, quizDao)
    }

    fun provideMentalHistoryRepository(context: Context): MentalHistoryRepository {
        val apiService = ApiConfig.getMentalHistoryApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.mentalHistoryDao()
        return MentalHistoryRepository.getInstance(apiService, dao)
    }

    fun provideSpecialistRepository(context: Context): SpecialistRepository {
        val apiService = ApiConfig.getSpecialistApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.specialistDao()
        return SpecialistRepository.getInstance(apiService, dao)
    }

}