package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.VehicleBrand
import retrofit2.Response
import retrofit2.http.GET

interface VehicleBrandService {
    @GET("GetAll")
    suspend fun getAllCarBrands(): Response<BackResponse<List<VehicleBrand>>>
}