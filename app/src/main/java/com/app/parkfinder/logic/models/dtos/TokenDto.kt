package com.app.parkfinder.logic.models.dtos

data class TokenDto (
    val accessToken: String?,
    val refreshToken: String?
)