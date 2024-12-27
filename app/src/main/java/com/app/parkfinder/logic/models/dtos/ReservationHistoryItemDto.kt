package com.app.parkfinder.logic.models.dtos

data class ReservationHistoryItemDto (
    val vehicleVehicleModelName : String ,
    val vehicleVehicleModelVehicleBrandName : String,
    val vehicleLicencePlate: String,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val rating: Int,
    val parkingSpotParkingLotRoad: String,
    val parkingSpotParkingLotCity: String,
)