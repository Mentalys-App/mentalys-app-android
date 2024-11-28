package com.mentalys.app.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {

        private const val BASE_URL_MAIN = "https://mentalys-restapi-62132417529.asia-southeast2.run.app/api/"
        private const val BASE_URL_ARTICLES = "https://api.abdisetiawan.my.id/"

        private fun createRetrofit(baseUrl: String): Retrofit {
            // To prevent data vulnerability
//            val loggingInterceptor = if(BuildConfig.DEBUG) {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//            }
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        fun getMainApiService(): MainApiService {
            return createRetrofit(BASE_URL_MAIN).create(MainApiService::class.java)
        }

        fun getArticlesApiService(): ArticlesApiService {
            return createRetrofit(BASE_URL_ARTICLES).create(ArticlesApiService::class.java)
        }

    }
}
