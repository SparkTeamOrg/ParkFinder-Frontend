package com.app.parkfinder.logic.models.dtos

data class ReservationCommentDto (
    val comment: String,
    val endTime: String,
    val rating: Int,
    val userFirstName: String,
    val userLastName: String,
    val userProfileImage: String?
)