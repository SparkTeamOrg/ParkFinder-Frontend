package com.app.parkfinder.logic.models.dtos

import java.io.Serializable

data class VehicleDto (
    var id: Int,
    var licencePlate: String,
    var color: String,
    var vehicleModelId: Int,
    var vehicleModelName: String,
    var vehicleModelVehicleBrandId: Int,
    var vehicleModelVehicleBrandName: String
) : Serializable