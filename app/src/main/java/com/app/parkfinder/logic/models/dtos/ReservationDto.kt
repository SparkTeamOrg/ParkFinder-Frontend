package com.app.parkfinder.logic.models.dtos

data class ReservationDto (
    val id: Int,
    val vehicleId: Int,
    val parkingSpotId: Int,
    val startTime: String,
    val isConfirmed: Boolean,
    val confirmationTime: String
)