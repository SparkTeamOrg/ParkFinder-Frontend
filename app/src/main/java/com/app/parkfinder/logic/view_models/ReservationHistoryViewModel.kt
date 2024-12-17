package com.app.parkfinder.logic.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationHistoryDto
import com.app.parkfinder.logic.models.dtos.ReservationCommentDto
import com.app.parkfinder.logic.models.dtos.ReservationHistoryApiResponse
import com.app.parkfinder.logic.models.dtos.ReservationHistoryDto
import com.app.parkfinder.logic.models.dtos.pagination.ReservationHistoryPaginationRequest
import com.app.parkfinder.logic.paging.ReservationHistoryPagingSource
import com.app.parkfinder.logic.services.ReservationHistoryService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ReservationHistoryViewModel : ViewModel() {
    private val reservationHistoryService =
        RetrofitConfig.createService(ReservationHistoryService::class.java)

    private val _parkingSpotCommentsResult = MutableLiveData<BackResponse<List<ReservationCommentDto>>>()
    private val _parkingSpotRatingResult = MutableLiveData<BackResponse<Double>>()
    private val _createReservationHistoryResult = MutableLiveData<ReservationHistoryApiResponse>()

    val parkingSpotCommentsResult: LiveData<BackResponse<List<ReservationCommentDto>>> = _parkingSpotCommentsResult
    val parkingSpotRatingResult: LiveData<BackResponse<Double>> = _parkingSpotRatingResult
    val createReservationHistoryResult: LiveData<ReservationHistoryApiResponse> = _createReservationHistoryResult

    private var _reservationHistoryPagingSource = MutableStateFlow<ReservationHistoryPagingSource?>(null)

    private val _filter = MutableLiveData(ReservationHistoryPaginationRequest(1, 10, "Date", true, "", null, null, null))
    private val filter: LiveData<ReservationHistoryPaginationRequest> = _filter

    val reservationHistories = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            ReservationHistoryPagingSource(reservationHistoryService, filter.value!!).also {
                _reservationHistoryPagingSource.value = it
            }
        }
    ).flow.cachedIn(viewModelScope)

    fun setFilter(startDate: String?, endDate: String?, sortBy: String, vehicleId: Int?, sortDescending: Boolean) {
        _filter.value = _filter.value?.copy(
            startDate = startDate,
            endDate = endDate,
            sortBy = sortBy,
            vehicleId = vehicleId,
            sortDescending = sortDescending
        )
    }

    fun getParkingSpotComments(parkingSpotId: Int) {
        viewModelScope.launch {
            try {
                val response = reservationHistoryService.getParkingSpotComments(parkingSpotId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _parkingSpotCommentsResult.postValue(it)
                    }
                } else {
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
                if (response.isSuccessful) {
                    response.body()?.let {
                        _parkingSpotRatingResult.postValue(it)
                    }
                } else {
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
                val response =
                    reservationHistoryService.addReservationHistory(createReservationHistoryDto)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _createReservationHistoryResult.postValue(it)
                    }
                } else {
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

    fun refreshReservationHistory() {
        _reservationHistoryPagingSource.value?.invalidate()
    }
}