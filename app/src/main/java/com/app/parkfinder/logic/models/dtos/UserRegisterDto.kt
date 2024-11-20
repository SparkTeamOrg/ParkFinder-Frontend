package com.app.parkfinder.logic.models.dtos

import okhttp3.MultipartBody

data class UserRegisterDto(
    val email : String,
    val password: String,
    val firstName: String,
    val mobilePhone: String,
    val profileImage: MultipartBody.Part?,
    val lastName: String,
    val licencePlate: String,
    val color: String,
    val modelId: Int,
    val verificationCode: String
)