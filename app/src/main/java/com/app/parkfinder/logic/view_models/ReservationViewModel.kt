package com.app.parkfinder.logic.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationDto
import com.app.parkfinder.logic.models.dtos.ReservationDto
import com.app.parkfinder.logic.services.ReservationService
import kotlinx.coroutines.launch
import java.util.Date

class ReservationViewModel: ViewModel() {
    private val reservationService = RetrofitConfig.createService(ReservationService:: class.java)

    private val _createReservationResult = MutableLiveData<BackResponse<Int>>()
    private val _confirmReservationResult = MutableLiveData<BackResponse<ReservationDto>>()
    private val _deleteReservationResult = MutableLiveData<BackResponse<Boolean>>()

    val createReservationResult: LiveData<BackResponse<Int>> = _createReservationResult
    val confirmReservationResult: LiveData<BackResponse<ReservationDto>> = _confirmReservationResult
    val deleteReservationResult: LiveData<BackResponse<Boolean>> = _deleteReservationResult

    fun createReservation(createReservationDto: CreateReservationDto) {
        viewModelScope.launch {
            try {
                val response = reservationService.createReservation(createReservationDto)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _createReservationResult.postValue(it)
                    }
                }
                else {
                   val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = -1
                    )
                    _createReservationResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = -1
                )
                _createReservationResult.postValue(errorResponse)
            }
        }
    }

    fun confirmReservation(id: Int) {
        viewModelScope.launch {
            try {
                val response = reservationService.confirmReservation(id)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _confirmReservationResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ReservationDto(-1,-1,-1, "", false, Date())
                    )
                    _confirmReservationResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                Log.d("Debug", e.toString())
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ReservationDto(-1,-1,-1, "", false, Date())
                )
                _confirmReservationResult.postValue(errorResponse)
            }
        }
    }

    fun deleteReservation(id: Int) {
        viewModelScope.launch {
            try {
                val response = reservationService.deleteReservation(id)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _deleteReservationResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = false
                    )
                    _deleteReservationResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = false
                )
                _deleteReservationResult.postValue(errorResponse)
            }
        }
    }
}