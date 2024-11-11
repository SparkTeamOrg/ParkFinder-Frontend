package com.app.parkfinder.logic.interceptor

import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.dtos.TokenDto
import com.app.parkfinder.logic.services.TokenService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.logging.Logger

class ApiInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenService = RetrofitConfig.createService(TokenService:: class.java)
        val originalRequest = chain.request()
        val accessToken = AppPreferences.accessToken

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        var response = chain.proceed(newRequest)
        if(response.code() == 401)
        {
            val refreshToken = AppPreferences.refreshToken
            val refreshResponse = runBlocking {
                    tokenService.refresh(TokenDto(accessToken,refreshToken))
            }

            if(refreshResponse.isSuccessful) {
                val body = refreshResponse.body()
                if(body != null){
                    val refreshSuccess = body.isSuccessful
                    if(refreshSuccess){
                        val newTokens = body.data
                        AppPreferences.accessToken = newTokens.accessToken
                        AppPreferences.refreshToken = newTokens.refreshToken
                        val retryRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer ${ newTokens.accessToken }")
                            .build()
                        Logger.getLogger("Interceptor").info("New Access Token: " +newTokens.accessToken)
                        Logger.getLogger("Interceptor").info("New Refresh Token: " +newTokens.refreshToken)
                        response = chain.proceed(retryRequest)
                    }
                    else{
                        AppPreferences.removeTokens()
                    }
                }
            }
        }

        return  response
    }
}