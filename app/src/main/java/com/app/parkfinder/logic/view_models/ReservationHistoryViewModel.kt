package com.app.parkfinder.logic.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ReservationCommentDto
import com.app.parkfinder.logic.services.ReservationHistoryService
import kotlinx.coroutines.launch

class ReservationHistoryViewModel : ViewModel() {
    private val reservationHistoryService = RetrofitConfig.createService(ReservationHistoryService:: class.java)

    private val _parkingSpotCommentsResult = MutableLiveData<BackResponse<List<ReservationCommentDto>>>()
    private val _parkingSpotRatingResult = MutableLiveData<BackResponse<Double>>()

    val parkingSpotCommentsResult: LiveData<BackResponse<List<ReservationCommentDto>>> = _parkingSpotCommentsResult
    val parkingSpotRatingResult: LiveData<BackResponse<Double>> = _parkingSpotRatingResult

    fun getParkingSpotComments(parkingSpotId: Int) {
        viewModelScope.launch {
            try {
                val response = reservationHistoryService.getParkingSpotComments(parkingSpotId)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _parkingSpotCommentsResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<ReservationCommentDto>()
                    )
                    _parkingSpotCommentsResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                Log.d("Debug", e.toString())
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = emptyList<ReservationCommentDto>()
                )
                _parkingSpotCommentsResult.postValue(errorResponse)
            }
        }
    }

    fun getParkingSpotRating(parkingSpotId: Int) {
        viewModelScope.launch {
            try {
                val response = reservationHistoryService.getParkingSpotRating(parkingSpotId)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _parkingSpotRatingResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = -1.0
                    )
                    _parkingSpotRatingResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = -1.0
                )
                _parkingSpotRatingResult.postValue(errorResponse)
            }
        }
    }
}