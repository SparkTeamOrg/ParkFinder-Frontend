package com.app.parkfinder.logic.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.parkfinder.logic.models.dtos.TransactionDto
import com.app.parkfinder.logic.services.BalanceService

class TransactionPagingSource(
    private val balanceService: BalanceService
) : PagingSource<Int, TransactionDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionDto> {
        return try {
            val page = params.key ?: 1
            val response = balanceService.getTransactions(page, params.loadSize)
            if (response.isSuccessful) {
                val transactions = response.data.items ?: emptyList()
                LoadResult.Page(
                    data = transactions,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (transactions.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error fetching transactions"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TransactionDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}