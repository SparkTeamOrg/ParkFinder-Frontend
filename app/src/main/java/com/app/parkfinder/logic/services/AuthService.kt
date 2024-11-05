package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.models.dtos.UserRegisterDto
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @POST("login")
    suspend fun login(@Body login: UserLoginDto): Response<BackResponse<String>>

    @POST("register")
    suspend fun register(@Body register: UserRegisterDto): Response<BackResponse<String>>
    @POST("code/register")
    suspend fun verificationCodeRegister(@Query("email") email:String): Response<BackResponse<String>>

    @POST("verify")
    suspend fun emailVerification(@Query("email") email: String, @Query("code") verificationCode: String): Response<BackResponse<String>>
}