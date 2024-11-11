package com.app.parkfinder.logic

import com.app.parkfinder.BuildConfig
import com.app.parkfinder.logic.Interceptor.ApiInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    private const val BACK_URL = BuildConfig.BACKEND_URL

    private val apiInterceptor = ApiInterceptor()

    //This allows all HTTP requests sent through Retrofit to use this interceptor for
    // automatically adding the token to the header and refreshing it if necessary.
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BACK_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    // Generic function to create a service
    // Usage: val exampleService = RetrofitConfig.createService(ExampleService::class.java)
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}