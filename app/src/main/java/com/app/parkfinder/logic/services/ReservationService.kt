package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationDto
import com.app.parkfinder.logic.models.dtos.ReservationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReservationService {
    @GET("reservation/all-confirmed")
    suspend fun getConfirmedReservation(
    ): Response<BackResponse<List<ReservationDto>>>

    @POST("reservation/new")
    suspend fun createReservation(
        @Body createReservationDto: CreateReservationDto
    ): Response<BackResponse<Int>>

    @PUT("reservation/confirm/{id}")
    suspend fun confirmReservation(
        @Path("id") id: Int
    ): Response<BackResponse<ReservationDto>>

    @DELETE("reservation/delete/{id}")
    suspend fun deleteReservation(
        @Path("id") id: Int
    ): Response<BackResponse<Boolean>>
}