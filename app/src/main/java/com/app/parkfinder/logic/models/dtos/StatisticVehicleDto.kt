package com.app.parkfinder.logic.models.dtos

data class StatisticVehicleDto(
    var brandName: String,
    var modelName: String,
    var reservationCountPerMonth: List<Int>,
    var moneySpentPerMonth: List<Double>
)