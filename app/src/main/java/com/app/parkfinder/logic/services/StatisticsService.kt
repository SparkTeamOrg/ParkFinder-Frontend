package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.StatisticDto
import retrofit2.Response
import retrofit2.http.GET

interface StatisticsService {
    @GET("statistics")
    suspend fun getUserStatistics(
    ): Response<BackResponse<StatisticDto>>
}