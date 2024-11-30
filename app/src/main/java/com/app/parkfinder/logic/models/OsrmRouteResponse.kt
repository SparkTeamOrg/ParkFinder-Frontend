package com.app.parkfinder.logic.models


//Look for documentation about OSRM
//https://github.com/Project-OSRM/osrm-backend/blob/master/docs/http.md#route-service

data class OsrmRouteResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: Geometry,
    val legs: List<Leg>
)

data class Leg(
    val summary: String,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val steps: List<Step>
)

data class Maneuver(
    val location : List<Double>,
    val modifier: String,
    val type: String,
    var exit: String?

)

data class Step(
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val maneuver: Maneuver,
    val name: String


)
data class Geometry(
    val coordinates: List<List<Double>> // [longitude, latitude] pairs
)

data class NavigationStep(
    val instruction: String, // Turn-by-turn instruction
    val distance: Double,    // Distance to the next step (in meters)
    val duration: Double     // Duration to the next step (in seconds)
)
