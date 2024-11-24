package com.app.parkfinder.logic.models

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
    val bearing_after: Int,
    val bearing_before: Int,
    val type: String

)

data class Step(
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val maneuver: Maneuver


)
data class Geometry(
    val coordinates: List<List<Double>> // [longitude, latitude] pairs
)