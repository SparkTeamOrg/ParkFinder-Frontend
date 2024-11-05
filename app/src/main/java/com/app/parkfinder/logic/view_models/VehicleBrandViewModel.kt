package com.app.parkfinder.logic.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.VehicleBrand
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import kotlinx.coroutines.launch

class VehicleBrandViewModel: ViewModel() {
    var brands = mutableStateOf<Map<Int,String>>(mapOf())
    fun getAllVehicleBrands() {
        viewModelScope.launch {
            val response = RetrofitConfig.vehicleBrandService.getAllCarBrands()
            if(response.isSuccessful){
                val body = response.body()
                for(brand in body?.data!!)
                {
                    brands.value += Pair<Int,String>(brand.id,brand.name)
                }

            }
            else
                println("request je neuspesan")
        }
    }
}