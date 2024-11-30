package com.app.parkfinder.logic.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.services.VehicleModelService
import kotlinx.coroutines.launch

class VehicleModelViewModel : ViewModel() {
    private val vehicleModelService = RetrofitConfig.createService(VehicleModelService::class.java)
    var vehicle_models = mutableStateOf<Map<Int,String>>(mapOf())

    fun getAllVehicleModelsByBrand(brandId:Int) {
        viewModelScope.launch {
            val response = vehicleModelService.getAllVehicleModelByBrandId(brandId)
            if(response.isSuccessful){
                val body = response.body()
                val newModels = body?.data?.associate { brand ->
                    brand.id to brand.name
                } ?: mapOf()

                vehicle_models.value = newModels
            }
            else
                println("request je neuspesan")
        }
    }
}