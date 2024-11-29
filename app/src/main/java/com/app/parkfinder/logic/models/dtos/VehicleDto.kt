package com.app.parkfinder.logic.models.dtos

data class VehicleDto (
    var id: Int,
    var licencePlate: String,
    var color: String,
    var vehicleModelId: Number,
    var vehicleModelName: String,
    var vehicleModelVehicleBrandId: Number,
    var vehicleModelVehicleBrandName: String
)