package com.app.parkfinder.logic.models.dtos

data class ParkingLotDto (
    var id : Int,
    var polygonGeoJson : String,
    var occupied : Int,
    var parkingZoneId: Int,
    var road: String,
    var town: String,
    var city: String,
    var county: String,
    var state: String,
    var postCode: Int,
    var country: String,
    var countryCode: String,
    var distance: Double
)