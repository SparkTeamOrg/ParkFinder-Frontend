package com.app.parkfinder.logic.models

data class BackResponse<T> (
    val data: T,
    val messages: List<String>,
    val isSuccessful: Boolean
)