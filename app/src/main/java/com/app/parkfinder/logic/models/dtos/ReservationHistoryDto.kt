package com.app.parkfinder.logic.models.dtos

data class ReservationHistoryDto (
    val id: Int,
    val userId: Int,
    val vehicleId: Int,
    val parkingSpotId: Int,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val rating: Int,
    val comment: String? = null
)