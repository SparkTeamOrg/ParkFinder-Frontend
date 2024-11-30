package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateVehicleDto
import com.app.parkfinder.logic.models.dtos.UpdateVehicleDto
import com.app.parkfinder.logic.models.dtos.VehicleDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface VehicleService {

    @GET("Vehicle/UserVehicles")
    suspend fun getUserVehicles(
        @Query("userId") userId: Int
    ):Response<BackResponse<List<VehicleDto>>>

    @POST("Vehicle/RegisterVehicle")
    suspend fun registerVehicle(
        @Body createVehicleDto: CreateVehicleDto
    ): Response<BackResponse<String>>

    @PUT("Vehicle/UpdateVehicle")
    suspend fun updateVehicle(
        @Body updateVehicleDto: UpdateVehicleDto
    ): Response<BackResponse<UpdateVehicleDto>>

    @DELETE("Vehicle/DeleteVehicle")
    suspend fun deleteVehicle(
        @Query("vehicleId") vehicleId: Int,
        @Query("userId") userId: Int
    ): Response<BackResponse<Int>>
}