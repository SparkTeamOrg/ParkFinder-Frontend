package com.app.parkfinder.logic.models.dtos

data class CreateVehicleDto (
    var licencePlate: String,
    var color: String,
    var modelId: Number
)