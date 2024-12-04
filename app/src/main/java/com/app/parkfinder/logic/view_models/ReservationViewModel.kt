package com.app.parkfinder.logic.view_models

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
    private val _createReservationResult = MutableLiveData<BackResponse<ReservationDto>>()
    val createReservationResult: LiveData<BackResponse<ReservationDto>> = _createReservationResult

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
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ReservationDto(-1,-1,-1, Date(), false, Date())
                    ).let {
                        _createReservationResult.postValue(
                            it
                        )
                    }
                }

                response.body()?.let {
                    _createReservationResult.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ReservationDto(-1,-1,-1, Date(), false, Date())
                ).let {
                    _createReservationResult.postValue(
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf(e.message ?: "An error occurred"),
                            data = ReservationDto(-1,-1,-1, Date(), false, Date())
                        )
                    )
                }
            }
        }
    }
}