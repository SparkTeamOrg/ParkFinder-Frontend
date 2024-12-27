package com.app.parkfinder.logic.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.parkfinder.logic.models.dtos.ReservationHistoryItemDto
import com.app.parkfinder.logic.models.dtos.pagination.ReservationHistoryPaginationRequest
import com.app.parkfinder.logic.services.ReservationHistoryService

class ReservationHistoryPagingSource(
    private val reservationHistoryService: ReservationHistoryService,
    private val filter: ReservationHistoryPaginationRequest
) : PagingSource<Int, ReservationHistoryItemDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReservationHistoryItemDto> {
        return try {
            val page = params.key ?: 1
            val response = reservationHistoryService.getPaginatedReservationHistory(
                page = page,
                startDate = filter.startDate,
                endDate = filter.endDate,
                sortBy = filter.sortBy,
                sortDescending = filter.sortDescending,
                vehicleId = filter.vehicleId,
                limit = filter.limit
            )
            if (response.isSuccessful) {
                val reservationHistories = response.body()?.data?.items ?: emptyList()
                LoadResult.Page(
                    data = reservationHistories,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (reservationHistories.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error fetching transactions"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ReservationHistoryItemDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}