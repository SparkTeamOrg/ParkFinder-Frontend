package com.app.parkfinder.logic.models.dtos.pagination

data class ReservationHistoryPaginationRequest (
    val page: Int,
    val limit: Int,
    val sortBy: String,
    val sortDescending: Boolean = false,
    val searchTerm: String,
    val vehicleId: Int?,
    val startDate: String?,
    val endDate: String?
)