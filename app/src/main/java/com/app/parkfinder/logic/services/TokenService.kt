package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.TokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface TokenService {
    @POST("token/refresh")
    suspend fun refresh(
        @Body refresh: TokenDto
    ): Response<BackResponse<TokenDto>>

    @DELETE("token/delete")
    suspend fun delete(
        @Query("userId") userId: Number
    ): Response<BackResponse<String>>
}