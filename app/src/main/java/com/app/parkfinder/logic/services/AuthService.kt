package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ResetPasswordDto
import com.app.parkfinder.logic.models.dtos.TokenDto
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("auth/login")
    suspend fun login(
        @Body login: UserLoginDto
    ): Response<BackResponse<TokenDto>>

    @Multipart
    @POST("auth/register")
    suspend fun register(
        @Part firstName: MultipartBody.Part,
        @Part lastName: MultipartBody.Part,
        @Part email: MultipartBody.Part,
        @Part mobilePhone: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part?,
        @Part password: MultipartBody.Part,
        @Part licencePlate: MultipartBody.Part,
        @Part color: MultipartBody.Part,
        @Part modelId: MultipartBody.Part,
        @Part verificationCode: MultipartBody.Part
    ): Response<BackResponse<String>>

    @POST("auth/code/register")
    suspend fun sendVerificationCodeForRegistration(
        @Query("email") email:String
    ): Response<BackResponse<String>>

    @POST("auth/code/reset")
    suspend fun sendVerificationCodeForPasswordReset(
        @Query("email") email:String
    ): Response<BackResponse<String>>

    @POST("auth/verify")
    suspend fun verifyVerificationCode(
        @Query("email") email: String,
        @Query("code") verificationCode: String
    ): Response<BackResponse<String>>

    @POST("auth/password/reset")
    suspend fun resetPassword(
        @Body resetPasswordDto: ResetPasswordDto
    ): Response<BackResponse<String>>
}