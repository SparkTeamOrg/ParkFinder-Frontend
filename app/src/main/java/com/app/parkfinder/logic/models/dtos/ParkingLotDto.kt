package com.app.parkfinder.logic.models.dtos

data class ParkingLotDto (
    var id : Int,
    var polygonGeoJson : String,
    var occupied : Int,
    var parkingZoneId: Int,
    var road: String,
    var town: String,
    var city: String,
    var County: String,
    var State: String,
    var PostCode: Int,
    var Country: String,
    var CountryCode: String
)