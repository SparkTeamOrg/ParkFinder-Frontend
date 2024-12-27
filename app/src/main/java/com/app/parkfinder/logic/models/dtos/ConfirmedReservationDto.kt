package com.app.parkfinder.logic.models.dtos

data class ConfirmedReservationDto (
    val confirmationTime: String,
    val vehicleId: Int,
    val vehicleLicencePlate: String,
    val vehicleVehicleModelName: String,
    val vehicleVehicleModelVehicleBrandName: String,
    val parkingSpotParkingLotCity: String,
    val parkingSpotParkingLotRoad: String,
    val parkingSpotParkingLotParkingZoneName: String
)