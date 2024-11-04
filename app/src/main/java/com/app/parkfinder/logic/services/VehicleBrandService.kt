package com.app.parkfinder.logic.services

import retrofit2.http.GET

interface VehicleBrandService {
    @GET("GetAll")
    suspend fun getAllCarBrands()
}