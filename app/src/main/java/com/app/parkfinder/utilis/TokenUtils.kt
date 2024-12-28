package com.app.parkfinder.utilis

import android.util.Log
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.models.dtos.UserDto
import com.auth0.android.jwt.JWT

fun decodeJwt(): UserDto {
    val token = AppPreferences.accessToken
    val dto = UserDto()
    try {
        val jwt = JWT(token!!)
        dto.Id = jwt.getClaim("UserId").asInt()!!
        dto.Fullname = jwt.getClaim("Fullname").asString()!!
        dto.Email = jwt.getClaim("Email").asString()!!
    } catch (e: Exception) {
        e.message?.let { Log.d("Debug", it) }
        e.printStackTrace()
    }
    return dto
}

fun isTokenExpired(token: String): Boolean {
    return try {
        val jwt = JWT(token)
        jwt.isExpired(10)   // 10 seconds lee-way to account for clock skew
    } catch (e: Exception) {
        true    // If there is an exception, the token is invalid
    }
}