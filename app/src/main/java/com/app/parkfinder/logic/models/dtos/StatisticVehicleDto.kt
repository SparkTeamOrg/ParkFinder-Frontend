package com.app.parkfinder.logic.models.dtos

data class StatisticVehicleDto(
    var brandName: String,
    var vehicleModel: String,
    var reservationCountPerMonth: List<Int>,
    var moneySpentPerMonth: List<Double>
)