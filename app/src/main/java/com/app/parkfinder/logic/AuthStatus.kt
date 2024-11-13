package com.app.parkfinder.logic

import androidx.lifecycle.MutableLiveData

object AuthStatus {
    val isRefreshTokenExpired = MutableLiveData(false)

    fun signalRefreshTokenExpired() {
        isRefreshTokenExpired.postValue(true)
    }

    fun resetRefreshTokenExpiredState() {
        isRefreshTokenExpired.postValue(false)
    }
}