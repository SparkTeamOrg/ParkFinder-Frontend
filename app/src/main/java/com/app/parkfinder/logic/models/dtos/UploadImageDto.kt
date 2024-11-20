package com.app.parkfinder.logic.models.dtos

import okhttp3.MultipartBody

data class UploadImageDto (
    val userId: Int?,
    val profileImage: MultipartBody.Part
)