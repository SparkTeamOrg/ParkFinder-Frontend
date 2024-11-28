package com.app.parkfinder.logic.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateVehicleDto
import com.app.parkfinder.logic.models.dtos.UpdateVehicleDto
import com.app.parkfinder.logic.models.dtos.VehicleDto
import com.app.parkfinder.logic.services.VehicleService
import kotlinx.coroutines.launch

class VehicleViewModel : ViewModel() {
    private val vehicleService = RetrofitConfig.createService(VehicleService:: class.java)
    private val _userVehiclesResult = MutableLiveData<BackResponse<List<VehicleDto>>>()
    private val _registerVehicleResult = MutableLiveData<BackResponse<String>>()
    private val _updateVehicleResult = MutableLiveData<BackResponse<UpdateVehicleDto>>()
    private val _deleteVehicleResult = MutableLiveData<BackResponse<Int>>()

    val userVehiclesResult: LiveData<BackResponse<List<VehicleDto>>> = _userVehiclesResult
    val registerVehicleResult: LiveData<BackResponse<String>> = _registerVehicleResult
    val updateVehicleResult: LiveData<BackResponse<UpdateVehicleDto>> = _updateVehicleResult
    val deleteVehicleResult: LiveData<BackResponse<Int>> = _deleteVehicleResult

    fun getUserVehicles(userId: Int) {
        viewModelScope.launch {
            try {
                val response = vehicleService.getUserVehicles(userId)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _userVehiclesResult.postValue(it)
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<VehicleDto>()
                    ).let {
                        _userVehiclesResult.postValue(
                            it
                        )
                    }
                }

                response.body()?.let {
                    _userVehiclesResult.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                ).let {
                    _userVehiclesResult.postValue(
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf(e.message ?: "An error occurred"),
                            data = emptyList<VehicleDto>()
                        )
                    )
                }
            }
        }
    }

    fun registerVehicle(createVehicleDto: CreateVehicleDto) {
        viewModelScope.launch {
            try {
                val response = vehicleService.registerVehicle(createVehicleDto)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _registerVehicleResult.postValue(it)
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    ).let {
                        _registerVehicleResult.postValue(
                            it
                        )
                    }
                }

                response.body()?.let {
                    _registerVehicleResult.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                ).let {
                    _registerVehicleResult.postValue(
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf(e.message ?: "An error occurred"),
                            data = ""
                        )
                    )
                }
            }
        }
    }

    fun updateVehicle(updateVehicleDto: UpdateVehicleDto) {
        viewModelScope.launch {
            try {
                val response = vehicleService.updateVehicle(updateVehicleDto)
                println(response)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _updateVehicleResult.postValue(it)
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = UpdateVehicleDto(-1,"","",-1,-1)
                    ).let {
                        _updateVehicleResult.postValue(
                            it
                        )
                    }
                }

                response.body()?.let {
                    _updateVehicleResult.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = UpdateVehicleDto(-1,"","",-1,-1)
                ).let {
                    _updateVehicleResult.postValue(
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf(e.message ?: "An error occurred"),
                            data = UpdateVehicleDto(-1,"","",-1,-1)
                        )
                    )
                }
            }
        }
    }

    fun deleteVehicle(vehicleId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                val response = vehicleService.deleteVehicle(vehicleId, userId)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _deleteVehicleResult.postValue(it)
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = -1
                    ).let {
                        _deleteVehicleResult.postValue(
                            it
                        )
                    }
                }

                response.body()?.let {
                    _deleteVehicleResult.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = -1
                ).let {
                    _deleteVehicleResult.postValue(
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf(e.message ?: "An error occurred"),
                            data = -1
                        )
                    )
                }
            }
        }
    }
}