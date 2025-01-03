package com.mentalys.app.di

import android.content.Context
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.local.room.MainDatabase
import com.mentalys.app.data.remote.retrofit.ApiConfig
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.data.repository.MentalHistoryRepository
import com.mentalys.app.data.repository.ClinicRepository
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.data.repository.MusicRepository
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

    fun provideClinicsRepository(context: Context): ClinicRepository {
        val apiService = ApiConfig.getClinicApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.clinicDao()
        return ClinicRepository.getInstance(apiService, dao)
    }

    fun provideMentalTestRepository(): MentalTestRepository {
        val apiService = ApiConfig.getMentalTestApiService()
        return MentalTestRepository.getInstance(apiService)
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

    fun provideMusicRepository(context: Context): MusicRepository {
        val apiService = ApiConfig.getMusicApiService()
        val database = MainDatabase.getInstance(context)
        val dao = database.musicDao()
        return MusicRepository.getInstance(apiService, dao)
    }

}