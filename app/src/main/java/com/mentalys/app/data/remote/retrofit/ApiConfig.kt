package com.mentalys.app.data.remote.retrofit

import com.mentalys.app.BuildConfig
import com.mentalys.app.utils.OAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {

        private const val BASE_URL_MAIN = BuildConfig.BASE_URL_MAIN
        private const val BASE_URL_ARTICLES = BuildConfig.BASE_URL_ARTICLES
        private const val BASE_URL_CLINIC = BuildConfig.BASE_URL_CLINIC
        private const val BASE_URL_SPECIALIST = "https://zyrridian.github.io/fake-api/"
        private const val BASE_URL_MUSIC = BuildConfig.BASE_URL_FREE_SOUND

        private fun createRetrofit(baseUrl: String, accessToken: String? = null): Retrofit {
            // Set up logging interceptor
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            // Set up OkHttpClient with OAuth Interceptor if accessToken is provided
            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)

            // Add OAuth Interceptor for authorization
            accessToken?.let {
                clientBuilder.addInterceptor(OAuthInterceptor(it))
            }

            val client = clientBuilder.build()

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

        fun getClinicApiService(): ClinicApiService {
            return createRetrofit(BASE_URL_CLINIC).create(ClinicApiService::class.java)
        }

        fun getSpecialistApiService(): SpecialistApiService {
            return createRetrofit(BASE_URL_MAIN).create(SpecialistApiService::class.java)
        }

        fun getMentalTestApiService(): MentalTestApiService {
            return createRetrofit(BASE_URL_MAIN).create(MentalTestApiService::class.java)
        }

        fun getMentalHistoryApiService(): MentalHistoryApiService {
            return createRetrofit(BASE_URL_MAIN).create(MentalHistoryApiService::class.java)
        }

        fun getMusicApiService(): MusicApiService {
            return createRetrofit(BASE_URL_MUSIC, BuildConfig.FREE_SOUND_API_KEY).create(MusicApiService::class.java)
        }

    }
}
