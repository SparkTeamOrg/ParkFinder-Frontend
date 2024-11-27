package com.app.parkfinder.logic.models.dtos

data class ParkingLotDto (
    var id : Int,
    var polygonGeoJson : String,
    var occupied : Int,
    var parkingZoneId: Int
)