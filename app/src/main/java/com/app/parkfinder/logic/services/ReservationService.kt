package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationDto
import com.app.parkfinder.logic.models.dtos.ReservationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationService {
    @POST("reservation/new")
    suspend fun createReservation(
        @Body createReservationDto: CreateReservationDto
    ): Response<BackResponse<ReservationDto>>
}