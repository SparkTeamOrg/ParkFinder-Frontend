package com.app.parkfinder.utilis

import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ParkingSpotStatusEnumDeserializer : JsonDeserializer<ParkingSpotStatusEnum> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ParkingSpotStatusEnum {
        return try {
            ParkingSpotStatusEnum.valueOf(json.asString)
        } catch (e: IllegalArgumentException) {
            ParkingSpotStatusEnum.FREE // Default value or handle the error as needed
        }
    }
}