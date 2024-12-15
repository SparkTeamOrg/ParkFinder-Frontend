package com.app.parkfinder.logic.models.dtos

data class StatisticDto(
    var months:List<Int>,
    var vehicles: List<StatisticVehicleDto>,
    var totalMoneySpent: Double,
    var reservationCount: Int,
    var averageReservationTime: Double
)