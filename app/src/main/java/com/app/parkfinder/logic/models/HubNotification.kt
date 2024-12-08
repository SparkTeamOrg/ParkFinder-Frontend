package com.app.parkfinder.logic.models

data class HubNotification<T>(
    var data: T,
    var sentDate: String
)