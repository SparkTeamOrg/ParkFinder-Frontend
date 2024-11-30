package com.app.parkfinder.logic.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.core.app.ApplicationProvider
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.NavigationStep
import com.app.parkfinder.logic.models.OsrmRouteResponse
import com.app.parkfinder.logic.models.Step
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.services.MapService
import com.app.parkfinder.logic.services.OsrmService
import com.app.parkfinder.ui.activities.NavigationActivity
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.util.LocationUtils
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.logging.Logger
import kotlin.math.cos
import kotlin.math.sin

class MapViewModel(application: Application) : AndroidViewModel(application), LocationListener {
    @SuppressLint("StaticFieldLeak")
    var mapView: MapView? = null

    private var steps: List<Step> = emptyList()
    var instructions = mutableListOf<NavigationStep>()

    // 0.03 is approximately 5km
    private val viewRadius: Double = 0.03 // User can see parking lots within radius of 0.03 degrees

    private var locationOverlay: MyLocationNewOverlay? = null
    private var lastLocation: GeoPoint? = null

    private var selectedRoute: Polyline? = null
    private var selectedPoint: GeoPoint? = null

    private var osrmRouteResponse : OsrmRouteResponse? = null

    private val osrmService = OsrmService.create()
    private val mapService = RetrofitConfig.createService(MapService::class.java)
    private val _getAllParkingLotsRes = MutableLiveData<BackResponse<List<ParkingLotDto>>>()
    private val _getAllInstructions = MutableLiveData<List<NavigationStep>>()

    val getAllParkingLotsRes: LiveData<BackResponse<List<ParkingLotDto>>> = _getAllParkingLotsRes
    val getAllInstructions : LiveData<List<NavigationStep>> = _getAllInstructions

    init {
        _getAllParkingLotsRes.observeForever { res ->
            if (res.isSuccessful) {
                mapView?.let { drawParkingLots(it, res.data) }
            }
        }
    }

    fun initializeMap(mapView: MapView) {
        this.mapView = mapView
        val locationProvider = GpsMyLocationProvider(getApplication())
        locationOverlay = MyLocationNewOverlay(locationProvider, mapView).apply {
            enableMyLocation()
            runOnFirstFix {
                myLocation?.let {
                    val initialLocation = GeoPoint(it.latitude, it.longitude)
                    lastLocation = initialLocation
                    mapView.controller.setCenter(initialLocation)
                    getNearbyParkingLots(it.latitude, it.longitude)
                    drawCircle(it.latitude, it.longitude)
                }
            }
        }
        mapView.overlays.add(locationOverlay)

        val locationManager = getApplication<Application>().getSystemService(LOCATION_SERVICE) as LocationManager

        // Request location updates
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L,
                10f,
                this
            )
        } catch (e: SecurityException) {
            Log.e("monkey","Location permission required")
        }

        // Disable zoom buttons
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.zoomController.setZoomInEnabled(false)
        mapView.zoomController.setZoomOutEnabled(false)

    }


    private fun getNearbyParkingLots(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                val response = mapService.GetAllNearbyParkingLots(viewRadius, lat, long)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _getAllParkingLotsRes.postValue(it)
                    }
                } else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<ParkingLotDto>()
                    ).let {
                        _getAllParkingLotsRes.postValue(it)
                    }
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = emptyList<ParkingLotDto>()
                ).let {
                    _getAllParkingLotsRes.postValue(it)
                }
            }
        }
    }

    // Function to fetch and draw route between start and end points
    private suspend fun drawRoute(mapView: MapView, start: GeoPoint, end: GeoPoint) :Polyline? {
        val startCoords = "${start.longitude},${start.latitude}"
        val endCoords = "${end.longitude},${end.latitude}"

        try {
            val response = withContext(Dispatchers.IO) {
                osrmService.getRoute(startCoords, endCoords).execute()
            }

            if (response.isSuccessful) {
                osrmRouteResponse = response.body()
                val geometry = osrmRouteResponse?.routes?.firstOrNull()?.geometry
                geometry?.let {
                    val geoPoints = it.coordinates.map { coord ->
                        GeoPoint(coord[1], coord[0])
                    }
                    val polyline = Polyline().apply {
                        setPoints(geoPoints)
                        outlinePaint.color = Color.rgb(0, 0, 255)
                    }

                    steps = osrmRouteResponse?.routes?.firstOrNull()?.legs?.firstOrNull()?.steps!!
                    instructions.clear()
                    var a  = 0
                    steps.map { step: Step ->


                        Log.e("${++a} TURN","Action: ${step.maneuver.type}, Name: ${step.name}, Direction: ${step.maneuver.modifier}, Exit: ${step.maneuver.exit}")

                        val direction = " " + step.maneuver.modifier // Add space before modifier if present
                        val instruction: String = when (step.maneuver.type) {
                            "turn" -> "Turn$direction onto ${step.name}."
                            "roundabout" -> {
                                val exitText = step.maneuver.exit?.let { " and take the $it exit" } ?: ""
                                "Enter the roundabout$direction$exitText."
                            }
                            "merge" -> "Merge$direction."
                            "on_ramp" -> "Take the ramp$direction."
                            "off_ramp" -> "Exit the ramp$direction."
                            "arrive" -> "You have arrived at your destination."
                            "depart" -> "Start on ${step.name}."
                            "continue" -> "Continue$direction onto ${step.name}."
                            "notification" -> "Notification: ${step.name}"  // Notifications often use `name` or other descriptive text
                            else -> "Follow the route."  // Default fallback
                        }
                        "$instruction (${step.distance.toInt()} meters)" // Append the distance

                        val item = NavigationStep(
                            distance = step.distance,
                            duration = step.duration,
                                instruction = instruction
                        )
                        instructions.add(item)
                    }
                    _getAllInstructions.postValue(instructions)
                    mapView.overlays.remove(selectedRoute)
//                    mapView.overlays.removeIf { overlay -> overlay is Polyline }
                    mapView.overlays.add(polyline)
                    polyline.setOnClickListener{_,_,_->
                        mapView.overlays.remove(selectedRoute)
                        instructions.clear()
                        _getAllInstructions.postValue(instructions)
                        selectedRoute = null
                        selectedPoint = null
                        true
                    }
                    mapView.invalidate()
                    return polyline
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    private fun drawParkingLots(mapView: MapView, pLots: List<ParkingLotDto>) {
        for (lot in pLots) {
            val jsonObject = JsonParser.parseString(lot.polygonGeoJson).asJsonObject
            val coordinatesArray = jsonObject.getAsJsonObject("geometry")
                .getAsJsonArray("coordinates")
                .get(0) // gets the first one in the file

            val geoPoints = mutableListOf<GeoPoint>()
            for (coordinate in coordinatesArray.asJsonArray) {
                val lng = coordinate.asJsonArray[0].asDouble
                val lat = coordinate.asJsonArray[1].asDouble
                geoPoints.add(GeoPoint(lat, lng))
            }

            val polygon = Polygon(mapView)
            polygon.points = geoPoints
            polygon.fillColor = Color.argb(60, 0, 0, 255) // Semi-transparent blue
            polygon.strokeColor = Color.RED
            polygon.strokeWidth = 2f


            polygon.setOnClickListener{_,_,_ ->
                if(lastLocation == null)
                    Log.d("Mes","Lokacija je null")
                viewModelScope.launch {
                    if(selectedRoute!=null)
                        mapView.overlays.remove(selectedRoute)
                    selectedPoint = calculateCentroid(geoPoints)
                    selectedRoute = drawRoute(mapView,lastLocation!!,selectedPoint!!)
                }


                true
            }


            // Add the Polygon to the MapView
            mapView.overlays.add(polygon)
        }
        mapView.invalidate() // Refresh the map
    }
    private fun calculateCentroid(points: List<GeoPoint>): GeoPoint {
        var centroidLat = 0.0
        var centroidLon = 0.0

        for (point in points) {
            centroidLat += point.latitude
            centroidLon += point.longitude
        }

        val totalPoints = points.size
        return GeoPoint(centroidLat / totalPoints, centroidLon / totalPoints)
    }


    private fun drawCircle(latitude: Double, longitude: Double) {
        val circle = Polygon(mapView)
        val points = mutableListOf<GeoPoint>()
        val numPoints = 100
        val radiusInMeters = this.viewRadius * 111000 // Convert degrees to meters

        for (i in 0 until numPoints) {
            val angle = 2 * Math.PI * i / numPoints
            val dx = radiusInMeters * cos(angle)
            val dy = radiusInMeters * sin(angle)
            val point = GeoPoint(latitude + (dy / 111000), longitude + (dx / (111000 * cos(Math.toRadians(latitude)))))
            points.add(point)
        }

        circle.points = points
        circle.fillColor = Color.argb(90, 190, 228, 235) // Semi-transparent light blue
        circle.strokeColor = Color.RED
        circle.strokeWidth = 2f

        // Disable default click behavior
        circle.setOnClickListener { _, _, _ -> true }

        mapView?.overlays?.add(circle)
        mapView?.invalidate() // Refresh the map
    }

    fun enableMyLocation() {
        locationOverlay?.enableMyLocation()
    }

    fun zoomIn() {
        mapView?.controller?.zoomIn()
    }

    fun zoomOut() {
        mapView?.controller?.zoomOut()
    }

    fun setCenterToMyLocation() {
        locationOverlay?.myLocation?.let {
            mapView?.controller?.setCenter(GeoPoint(it.latitude, it.longitude))
        }
    }

    fun destroy() {
        locationOverlay?.disableMyLocation()
        super.onCleared()
    }

    override fun onLocationChanged(loc: Location) {
        Log.e("monkey","location changed Lon:${loc.longitude} Lat:${loc.latitude}")
        val newLocation = GeoPoint(loc.latitude, loc.longitude)
        Log.d("monkey","Location changed to: $newLocation")
//        if (newLocation.distanceToAsDouble(lastLocation) > 10) {
            lastLocation = newLocation
            mapView?.overlays?.clear()
            mapView?.controller?.setCenter(newLocation)
            getNearbyParkingLots(loc.latitude, loc.longitude)
            drawCircle(loc.latitude, loc.longitude)

            viewModelScope.launch {
                if(selectedRoute!=null)
                    mapView?.overlays?.remove(selectedRoute)
                if(selectedPoint!=null)
                    selectedRoute = drawRoute(mapView!!,newLocation,selectedPoint!!);
//            }
        }
    }
}