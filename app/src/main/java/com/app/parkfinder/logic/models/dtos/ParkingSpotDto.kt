package com.app.parkfinder.logic.models.dtos

data class ParkingSpotDto (
    var id : Int,
    var polygonGeoJson : String,
    var parkingSpotStatus: Int,
    var parkingLotId: Int
)