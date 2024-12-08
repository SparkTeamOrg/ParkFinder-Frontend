package com.app.parkfinder.logic.models.dtos

data class UserLocationDto(
    var longitude: Double,
    var latitude: Double,
    var fpmOn: Boolean
)