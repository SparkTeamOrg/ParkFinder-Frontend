package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.VehicleModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleModelService {
    @GET("GetByBrandId/{brandId}")
    suspend fun GetAllVehicleModelByBrandId(@Path("brandId") brandId: Int): Response<BackResponse<List<VehicleModel>>>
}