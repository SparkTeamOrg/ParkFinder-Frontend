package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.TransactionDto
import retrofit2.Response
import retrofit2.http.GET

interface BalanceService {
    @GET("transaction/user/balance")
    suspend fun getUserBalance(
    ): Response<BackResponse<Double>>

    @GET("transaction/user/")
    suspend fun getUserTransactions(
    ): Response<BackResponse<List<TransactionDto>>>

}