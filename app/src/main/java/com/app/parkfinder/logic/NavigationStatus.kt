package com.app.parkfinder.logic

import androidx.lifecycle.MutableLiveData

object NavigationStatus {
    val isParkingSpotReserved = MutableLiveData<Int?>()

    fun signalParkingSpotReserved(spotNumber: Int) {
        isParkingSpotReserved.postValue(spotNumber)
    }

    fun signalParkingSpotLeft() {
        isParkingSpotReserved.postValue(null)
    }
}