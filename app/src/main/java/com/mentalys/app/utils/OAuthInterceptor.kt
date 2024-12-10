package com.mentalys.app.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorizedRequest = originalRequest.newBuilder()
            .header("Authorization", "Token $accessToken")
            .build()
        Log.d("OAuthInterceptor", "Authorization Header: ${authorizedRequest.headers}")
        return chain.proceed(authorizedRequest)
    }
}
