package com.app.parkfinder.logic

import androidx.lifecycle.MutableLiveData

object NavigationStatus {
    val isParkingSpotReserved = MutableLiveData(false)

    fun signalParkingSpotReserved() {
        isParkingSpotReserved.postValue(true)
    }

    fun signalParkingSpotLeft() {
        isParkingSpotReserved.postValue(false)
    }
}