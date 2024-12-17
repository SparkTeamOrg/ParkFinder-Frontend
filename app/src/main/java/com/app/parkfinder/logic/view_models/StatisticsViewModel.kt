package com.app.parkfinder.logic.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.StatisticDto
import com.app.parkfinder.logic.services.StatisticsService
import kotlinx.coroutines.launch

class StatisticsViewModel: ViewModel()  {
    private val _getStatistic = MutableLiveData<BackResponse<StatisticDto>>()
    var getStatistic: LiveData<BackResponse<StatisticDto>> = _getStatistic


    private val statisticsService =  RetrofitConfig.createService(StatisticsService::class.java)

    fun getUserStatistics()
    {
        viewModelScope.launch {
            try {
                val response = statisticsService.getUserStatistics()
                if(response.isSuccessful) {
                    response.body()?.let {
                        _getStatistic.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = StatisticDto(
                            months = emptyList(),
                            vehicles = emptyList(),
                            totalMoneySpent = -1.0,
                            reservationCount = -1,
                            averageReservationTime = -1.0
                        )
                    )
                    _getStatistic.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf("An error occurred"),
                    data = StatisticDto(
                        months = emptyList(),
                        vehicles = emptyList(),
                        totalMoneySpent = -1.0,
                        reservationCount = -1,
                        averageReservationTime = -1.0
                    )
                )
                _getStatistic.postValue(errorResponse)
            }
        }
    }
}