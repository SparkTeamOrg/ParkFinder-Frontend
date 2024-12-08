package com.app.parkfinder.logic.models.dtos

import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum

data class ParkingSpotUpdateNotificationDto (
    val parkingSpotId: Int,
    val parkingSpotStatus: Int
) {
    fun getParkingSpotStatusEnum(): ParkingSpotStatusEnum {
        return ParkingSpotStatusEnum.entries.firstOrNull { it.ordinal == parkingSpotStatus }
            ?: ParkingSpotStatusEnum.FREE // Default value or handle the error as needed
    }
}