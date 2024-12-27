package com.app.parkfinder.logic.enums

enum class ParkingSpotStatusEnum(val value:Int) {
    FREE(0),
    RESERVED(1),
    OCCUPIED(2),
    TEMPORARILY_UNAVAILABLE(3),
    OCCUPIED_BY_SIMULATION(4);

    companion object{
        private val map = entries.associateBy(ParkingSpotStatusEnum::value)

        fun fromValue(value: Int): ParkingSpotStatusEnum? = map[value]
    }
}