package com.app.parkfinder.logic.models.dtos

import com.app.parkfinder.logic.models.BackResponse

data class ReservationHistoryApiResponse(
    val item1: BackResponse<ReservationHistoryDto>,
    val item2: Int
)