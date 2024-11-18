package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    @GET("parking/nearby")
    suspend fun GetAllNearbyParkingLots(
        @Query("radius") radius:Double,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double) : Response<BackResponse<List<ParkingLotDto>>>
}