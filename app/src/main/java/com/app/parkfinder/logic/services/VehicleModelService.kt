package com.app.parkfinder.logic.services

import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleModelService {
    @GET("GetByBrandId/{brandId}")
    suspend fun GetAllVehicleModelByBrandId(@Path("brandId") brandId: Int)
}