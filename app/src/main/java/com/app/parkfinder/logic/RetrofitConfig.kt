package com.app.parkfinder.logic

import com.app.parkfinder.logic.services.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    val back_url = "http://10.0.2.2:5009/"


    //kreiranje servisa
    val authService: AuthService by lazy {
        Retrofit
            .Builder()
            .baseUrl(back_url+"Auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}