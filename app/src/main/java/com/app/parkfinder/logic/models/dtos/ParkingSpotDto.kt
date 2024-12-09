package com.app.parkfinder.logic.models.dtos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkingSpotDto (
    var id : Int,
    var polygonGeoJson : String,
    var parkingSpotStatus: Int,
    var parkingLotId: Int,
    var rating: Double = 0.0
): Parcelable