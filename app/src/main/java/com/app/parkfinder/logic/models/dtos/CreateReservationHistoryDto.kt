package com.app.parkfinder.logic.models.dtos

data class CreateReservationHistoryDto (
    val vehicleId: Int,
    val rating: Int,
    val comment: String?
)