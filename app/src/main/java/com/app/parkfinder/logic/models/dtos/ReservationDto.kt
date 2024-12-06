package com.app.parkfinder.logic.models.dtos

import java.util.Date

data class ReservationDto (
    val id: Int,
    val vehicleId: Int,
    val parkingSpotId: Int,
    val startTime: Date,
    val isConfirmed: Boolean,
    val confirmationTime: Date
)