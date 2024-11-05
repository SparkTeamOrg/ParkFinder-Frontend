package com.app.parkfinder.logic

import com.app.parkfinder.logic.services.AuthService
import com.app.parkfinder.logic.services.VehicleBrandService
import com.app.parkfinder.logic.services.VehicleModelService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    val back_url = "http://10.0.2.2:5009/"//back url


    //service creation
    val authService: AuthService by lazy {
        Retrofit
            .Builder()
            .baseUrl(back_url+"auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    val vehicleBrandService: VehicleBrandService by lazy {
        Retrofit
            .Builder()
            .baseUrl(back_url+"VehicleBrand/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehicleBrandService::class.java)
    }
    val vehicleModelService: VehicleModelService by lazy {
        Retrofit
            .Builder()
            .baseUrl(back_url+"api/VehicleModel/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehicleModelService::class.java)
    }
}