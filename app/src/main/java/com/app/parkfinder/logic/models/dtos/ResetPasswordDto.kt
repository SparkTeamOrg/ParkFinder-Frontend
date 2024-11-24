package com.app.parkfinder.logic.models.dtos

data class ResetPasswordDto (
    val email: String,
    val code: String,
    val newPassword: String
)