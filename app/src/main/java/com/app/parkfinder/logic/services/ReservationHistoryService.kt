package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ReservationCommentDto
import retrofit2.Response
import retrofit2.http.GET
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
}