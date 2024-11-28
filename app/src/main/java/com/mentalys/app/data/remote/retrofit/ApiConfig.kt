package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {

        private const val BASE_URL_MAIN = BuildConfig.BASE_URL_MAIN
        private const val BASE_URL_ARTICLES = BuildConfig.BASE_URL_ARTICLES

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
