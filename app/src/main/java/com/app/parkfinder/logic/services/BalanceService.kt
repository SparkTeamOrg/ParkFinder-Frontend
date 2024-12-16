package com.app.parkfinder.logic.services

import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.AddTransactionDto
import com.app.parkfinder.logic.models.dtos.TransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BalanceService {
    @GET("transaction/user/balance")
    suspend fun getUserBalance(
    ): Response<BackResponse<Double>>

    @GET("transaction/user/paginated")
    suspend fun getTransactions(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): Response<BackResponse<BasePaginationResultTemp<TransactionDto>>>

    @POST("transaction/add")
    suspend fun addBalance(
        @Body addTransactionDto: AddTransactionDto
    ): Response<BackResponse<TransactionDto?>>
}

data class BasePaginationResultTemp<T>(
    val totalItems: Int,
    val totalPages: Int,
    val page: Int,
    val limit: Int,
    val items: List<T>
)