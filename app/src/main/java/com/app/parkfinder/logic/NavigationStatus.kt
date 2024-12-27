package com.app.parkfinder.logic

import androidx.lifecycle.MutableLiveData
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto

object NavigationStatus {
    val isParkingSpotReserved = MutableLiveData<Int?>()
    val isSpotSelected = MutableLiveData<ParkingSpotDto?>()
    fun signalParkingSpotReserved(reservationId: Int, spot: ParkingSpotDto?) {
        isParkingSpotReserved.postValue(reservationId)
        isSpotSelected.postValue(spot)
    }
}