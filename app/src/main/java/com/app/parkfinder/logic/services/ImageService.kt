package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.RemoveImageDto
import com.app.parkfinder.logic.models.dtos.UploadImageDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ImageService {
    @GET("images/profileImage")
    suspend fun getProfileImage(
        @Query("userId") userId:Int
    ): Response<BackResponse<String>>

    @POST("images/upload")
    suspend fun uploadImage(
        @Body uploadImageDto: UploadImageDto
    ): Response<BackResponse<String>>

    @DELETE("images/delete")
    suspend fun removeImage(
        @Body removeImageDto: RemoveImageDto
    ): Response<BackResponse<String>>
}