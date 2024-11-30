package com.app.parkfinder.logic.models.dtos

data class UpdateVehicleDto(
    var id: Number,
    var licencePlate: String,
    var color: String,
    var userId: Number,
    var modelId: Number
)
