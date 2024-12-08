package com.app.parkfinder.logic

import androidx.lifecycle.MutableLiveData

object NavigationStatus {
    val isParkingSpotReserved = MutableLiveData<Int?>()

    fun signalParkingSpotReserved(reservationId: Int) {
        isParkingSpotReserved.postValue(reservationId)
    }
}