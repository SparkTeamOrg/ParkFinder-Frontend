package com.app.parkfinder.logic.models.dtos

data class CreateReservationDto (
    val vehicleId: Int,
    val parkingSpotId: Int
)