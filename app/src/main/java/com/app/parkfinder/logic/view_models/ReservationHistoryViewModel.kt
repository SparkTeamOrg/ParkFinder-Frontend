package com.app.parkfinder.logic.view_models

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationHistoryDto
import com.app.parkfinder.logic.models.dtos.ReservationCommentDto
import com.app.parkfinder.logic.models.dtos.ReservationHistoryApiResponse
import com.app.parkfinder.logic.models.dtos.ReservationHistoryDto
import com.app.parkfinder.logic.models.dtos.ReservationHistoryItemDto
import com.app.parkfinder.logic.models.dtos.pagination.BasePaginationResult
import com.app.parkfinder.logic.models.dtos.pagination.ReservationHistoryPaginationRequest
import com.app.parkfinder.logic.services.ReservationHistoryService
import kotlinx.coroutines.launch

class ReservationHistoryViewModel : ViewModel() {
    private val reservationHistoryService = RetrofitConfig.createService(ReservationHistoryService:: class.java)

    private val _parkingSpotCommentsResult = MutableLiveData<BackResponse<List<ReservationCommentDto>>>()
    private val _parkingSpotRatingResult = MutableLiveData<BackResponse<Double>>()
    private val _createReservationHistoryResult = MutableLiveData<ReservationHistoryApiResponse>()
    private val _paginateReservationHistoryResult = MutableLiveData<BackResponse<BasePaginationResult<ReservationHistoryItemDto>>>()

    val parkingSpotCommentsResult: LiveData<BackResponse<List<ReservationCommentDto>>> = _parkingSpotCommentsResult
    val parkingSpotRatingResult: LiveData<BackResponse<Double>> = _parkingSpotRatingResult
    val createReservationHistoryResult: LiveData<ReservationHistoryApiResponse> = _createReservationHistoryResult
    val paginateReservationHistoryResult: LiveData<BackResponse<BasePaginationResult<ReservationHistoryItemDto>>> = _paginateReservationHistoryResult

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

    fun addReservationHistory(createReservationHistoryDto: CreateReservationHistoryDto) {
        viewModelScope.launch {
            try {
                val response = reservationHistoryService.addReservationHistory(createReservationHistoryDto)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _createReservationHistoryResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = ReservationHistoryApiResponse(
                        item1 = BackResponse(
                            isSuccessful = false,
                            messages = emptyList(),
                            data = ReservationHistoryDto(-1, -1, -1, -1, "", "", 0.0, -1)
                        ), -1
                    )
                    _createReservationHistoryResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = ReservationHistoryApiResponse(
                    item1 = BackResponse(
                        isSuccessful = false,
                        messages = emptyList(),
                        data = ReservationHistoryDto(-1, -1, -1, -1, "", "", 0.0, -1)
                    ), -1
                )
                _createReservationHistoryResult.postValue(errorResponse)
            }
        }
    }

    fun getPaginatedReservationHistory(request: ReservationHistoryPaginationRequest) {

        viewModelScope.launch {
            try {
                val response = reservationHistoryService.getPaginatedReservationHistory(
                    page = request.page,
                    limit = request.limit,
                    sortBy = request.sortBy,
                    sortDescending = request.sortDescending,
                    vehicleId = request.vehicleId,
                    startDate = request.startDate,
                    endDate = request.endDate
                )
                if(response.isSuccessful) {
                    response.body()?.let {
                        _paginateReservationHistoryResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = BasePaginationResult(-1,-1,-1,-1, emptyList<ReservationHistoryItemDto>())
                    )
                    _paginateReservationHistoryResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = BasePaginationResult(-1,-1,-1,-1, emptyList<ReservationHistoryItemDto>())
                )
                _paginateReservationHistoryResult.postValue(errorResponse)
            }
        }
    }
}