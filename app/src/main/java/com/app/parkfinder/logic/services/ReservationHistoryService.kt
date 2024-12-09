package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationHistoryDto
import com.app.parkfinder.logic.models.dtos.ReservationCommentDto
import com.app.parkfinder.logic.models.dtos.ReservationHistoryApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationHistoryService {
    @GET("reservation-history/parking-spot-comments/{parkingSpotId}")
    suspend fun getParkingSpotComments(
        @Path("parkingSpotId") parkingSpotId: Int
    ): Response<BackResponse<List<ReservationCommentDto>>>

    @GET("reservation-history/parking-spot-rating/{parkingSpotId}")
    suspend fun getParkingSpotRating(
        @Path("parkingSpotId") parkingSpotId: Int
    ):Response<BackResponse<Double>>

    @POST("reservation-history/")
    suspend fun addReservationHistory(
        @Body createReservationHistoryDto: CreateReservationHistoryDto
    ):Response<ReservationHistoryApiResponse>
}