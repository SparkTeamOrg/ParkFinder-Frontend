package com.app.parkfinder.logic.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.os.Looper
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.NavigationStep
import com.app.parkfinder.logic.models.OsrmRouteResponse
import com.app.parkfinder.logic.models.Step
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.services.MapService
import com.app.parkfinder.logic.services.OsrmService
import com.app.parkfinder.utilis.TextOverlay
import com.google.gson.JsonParser
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.logging.Logger
import kotlin.math.cos
import kotlin.math.sin

class MapViewModel(application: Application) : AndroidViewModel(application), IMyLocationConsumer {
    @SuppressLint("StaticFieldLeak")
    var mapView: MapView? = null

    private var hubConnection: HubConnection? = null

    private var steps: List<Step> = emptyList()
    private var instructions = mutableListOf<NavigationStep>()

    // 0.03 is approximately 5km
    private val viewRadius: Double = 0.03 // User can see parking lots within radius of 0.03 degrees

    private var locationOverlay: MyLocationNewOverlay? = null
    private var lastLocation: GeoPoint? = null

    private var selectedRoute: Polyline? = null
    private var selectedPoint: GeoPoint? = null

    private var osrmRouteResponse : OsrmRouteResponse? = null
    private var currentParkingSpotOverlays: MutableList<Overlay> = mutableListOf()
    private var currentTextOverlays: MutableList<TextOverlay> = mutableListOf()
    private var currentParkingLotClickedId: Int = -1

    private val osrmService = OsrmService.create()
    private val mapService = RetrofitConfig.createService(MapService::class.java)
    private val _getAllParkingLotsRes = MutableLiveData<BackResponse<List<ParkingLotDto>>>()
    private val _getParkingSpotsForParkingLot = MutableLiveData<BackResponse<List<ParkingSpotDto>>>()
    private val _getAllInstructions = MutableLiveData<List<NavigationStep>>()

    val getAllInstructions : LiveData<List<NavigationStep>> = _getAllInstructions

    init {
        _getAllParkingLotsRes.observeForever { res ->
            if (res.isSuccessful) {
                mapView?.let { drawParkingLots(it, res.data) }
            }
        }

        _getParkingSpotsForParkingLot.observeForever { res ->
            if (res.isSuccessful) {
                mapView?.let { drawParkingSpots(it, res.data) }
            }
        }
    }

    private fun startHubConnection() {
        if (hubConnection == null) {
            try {
                // Create a new HubConnection
                hubConnection = HubConnectionBuilder.create("http://10.0.2.2:5009/baseHub")
                    .withTransport(com.microsoft.signalr.TransportEnum.LONG_POLLING)
                    .withAccessTokenProvider(Single.defer {
                        Single.just((AppPreferences.accessToken) ?: "")
                    })
                    .build()

                hubConnection?.on("GetParkingSpots",
                    { data ->
                        Logger.getLogger("MapViewModel").info(data.toString())

                        // Show a toast message on the UI thread instead of logging
                        // TODO: Replace with a more appropriate message handling
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(getApplication(), "Received message: $data", Toast.LENGTH_SHORT).show()
                        }
                    },
                    String::class.java
                )

                // Start the connection
                hubConnection?.start()?.blockingAwait()
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error starting hub connection", e)
                if (e.message?.contains("Unauthorized") == true) {
                    Toast.makeText(getApplication(), "Unauthorized access. Please log in again.", Toast.LENGTH_SHORT).show()
                    // Handle unauthorized access, e.g., redirect to login screen
                } else {
                    Toast.makeText(getApplication(), "Error starting hub connection: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopHubConnection() {
        hubConnection?.stop()
        hubConnection = null
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

        // Disable zoom buttons
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.zoomController.setZoomInEnabled(false)
        mapView.zoomController.setZoomOutEnabled(false)

        // Set location change listener
        locationProvider.startLocationProvider(this)

        startHubConnection()
    }

    override fun onLocationChanged(location: Location?, source: IMyLocationProvider?) {
        Log.d("monkey","Location changed")
        location?.let {
            val newLocation = GeoPoint(it.latitude, it.longitude)
            Log.d("monkey","Location changed to: $newLocation")
            if (newLocation.distanceToAsDouble(lastLocation) > 10) {
                lastLocation = newLocation
                mapView?.overlays?.clear()
                mapView?.controller?.setCenter(newLocation)
                getNearbyParkingLots(it.latitude, it.longitude)
                drawCircle(it.latitude, it.longitude)

                viewModelScope.launch {
                    if(selectedRoute!=null)
                        mapView?.overlays?.remove(selectedRoute)
                    selectedRoute = drawRoute(mapView!!,newLocation,selectedPoint!!)
                }
            }
        }
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

    private fun getParkingSpotsForParkingLot(parkingLotId: Int) {
        viewModelScope.launch {
            try {
                val response = mapService.getParkingSpotsForParkingLot(parkingLotId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _getParkingSpotsForParkingLot.postValue(it)
                    }
                } else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<ParkingSpotDto>()
                    ).let {
                        _getParkingSpotsForParkingLot.postValue(it)
                    }
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = emptyList<ParkingSpotDto>()
                ).let {
                    _getParkingSpotsForParkingLot.postValue(it)
                }
            }
        }
    }

    // Function to fetch and draw route between start and end points
    private suspend fun drawRoute(mapView: MapView, start: GeoPoint, end: GeoPoint) :Polyline? {
        val startCoordinates = "${start.longitude},${start.latitude}"
        val endCoordinates = "${end.longitude},${end.latitude}"

        try {
            val response = withContext(Dispatchers.IO) {
                osrmService.getRoute(startCoordinates, endCoordinates).execute()
            }

            if (response.isSuccessful) {
                osrmRouteResponse = response.body()
                val geometry = osrmRouteResponse?.routes?.firstOrNull()?.geometry
                geometry?.let {
                    val geoPoints = it.coordinates.map { cord ->
                        GeoPoint(cord[1], cord[0])
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
                if (currentParkingLotClickedId != lot.id) {
                    currentParkingLotClickedId = lot.id
                    getParkingSpotsForParkingLot(lot.id)
                }
                else {
                    clearParkingSpots()
                    currentParkingLotClickedId = -1
                }

                true
            }


            // Add the Polygon to the MapView
            mapView.overlays.add(polygon)
        }
        mapView.invalidate() // Refresh the map
    }

    private fun drawParkingSpots(mapView: MapView, pLots: List<ParkingSpotDto>) {

        // Clear existing parking spot overlays
        for (overlay in currentParkingSpotOverlays) {
            mapView.overlays.remove(overlay)
        }
        currentParkingSpotOverlays.clear()

        // Clear existing text overlays
        for (overlay in currentTextOverlays) {
            mapView.overlays.remove(overlay)
        }
        currentTextOverlays.clear()

        var spotNumber = 1
        for(spot in pLots) {
            val jsonObject = JsonParser.parseString(spot.polygonGeoJson).asJsonObject
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

            when (spot.parkingSpotStatus) {
                ParkingSpotStatusEnum.FREE.ordinal -> {
                    // Green
                    polygon.fillColor = Color.argb(100, 0, 255, 0)
                }
                ParkingSpotStatusEnum.RESERVED.ordinal -> {
                    // Yellow
                    polygon.fillColor = Color.argb(100, 255, 255, 0)
                }
                ParkingSpotStatusEnum.OCCUPIED.ordinal -> {
                    // Red
                    polygon.fillColor = Color.argb(100, 255, 0, 0)
                }
                else -> {
                    // Blue
                    polygon.fillColor = Color.argb(100, 0, 0, 255)
                }
            }

            polygon.strokeColor = Color.RED
            polygon.strokeWidth = 2f

            polygon.setOnClickListener{_,_,_ ->
                if (spot.parkingSpotStatus == ParkingSpotStatusEnum.FREE.ordinal
                    || spot.parkingSpotStatus == ParkingSpotStatusEnum.RESERVED.ordinal) {

                    if(lastLocation == null)
                        Log.d("Mes","Lokacija je null")

                    viewModelScope.launch {
                        if(selectedRoute!=null)
                            mapView.overlays.remove(selectedRoute)
                        selectedPoint = calculateCentroid(geoPoints)
                        selectedRoute = drawRoute(mapView,lastLocation!!,selectedPoint!!)
                    }
                }
                else if (spot.parkingSpotStatus == ParkingSpotStatusEnum.OCCUPIED.ordinal) {
                    Toast.makeText(getApplication(), "Parking spot is occupied", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(getApplication(), "Parking spot is temporarily unavailable", Toast.LENGTH_SHORT).show()
                }

                true
            }

            // Add the Polygon to the MapView
            mapView.overlays.add(polygon)
            currentParkingSpotOverlays.add(polygon)

            // Add text overlay with spot number
            val textOverlay = TextOverlay(
                text = "P" + spotNumber.toString(),
                position = calculateCentroid(geoPoints),
                textPaint = Paint(
                    Paint.ANTI_ALIAS_FLAG
                )
            )
            textOverlay.textPaint.color = Color.BLACK
            textOverlay.textPaint.textSize = 30f

            mapView.overlays.add(textOverlay)
            currentTextOverlays.add(textOverlay)

            spotNumber++
        }
        mapView.invalidate() // Refresh the map
    }

    private fun clearParkingSpots() {
        // Clear existing parking spot overlays
        for (overlay in currentParkingSpotOverlays) {
            mapView?.overlays?.remove(overlay)
        }
        currentParkingSpotOverlays.clear()

        // Clear existing text overlays
        for (overlay in currentTextOverlays) {
            mapView?.overlays?.remove(overlay)
        }
        currentTextOverlays.clear()

        mapView?.invalidate() // Refresh the map
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
        stopHubConnection()
        super.onCleared()
    }
}