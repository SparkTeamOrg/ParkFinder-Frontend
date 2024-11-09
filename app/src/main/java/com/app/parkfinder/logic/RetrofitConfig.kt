package com.app.parkfinder.logic

import com.app.parkfinder.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    private const val BACK_URL = BuildConfig.BACKEND_URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BACK_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Generic function to create a service
    // Usage: val exampleService = RetrofitConfig.createService(ExampleService::class.java)
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}