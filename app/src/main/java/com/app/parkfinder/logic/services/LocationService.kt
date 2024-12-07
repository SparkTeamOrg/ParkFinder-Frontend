package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.UserLocationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface LocationService {

    @PUT("location/updateLocation")
    suspend fun updateUserLocation(
        @Body updateLocationDto: UserLocationDto
    ): Response<BackResponse<Boolean>>
}