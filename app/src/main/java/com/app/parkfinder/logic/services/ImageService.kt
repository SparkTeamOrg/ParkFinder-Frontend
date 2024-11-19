package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageService {
    @GET("image/profileImage")
    suspend fun getProfileImage(
        @Query("userId") userId:Int
    ): Response<BackResponse<String>>

    @Multipart
    @POST("image/upload")
    suspend fun uploadImage(
        @Part userId: MultipartBody.Part,
        @Part profileImage: MultipartBody.Part
    ): Response<BackResponse<String>>

    @DELETE("image/delete")
    suspend fun removeImage(
        @Query("userId") userId: Int
    ): Response<BackResponse<String>>
}