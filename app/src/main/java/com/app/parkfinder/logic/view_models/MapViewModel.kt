package com.app.parkfinder.logic.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context.LOCATION_SERVICE
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.os.Looper
import android.os.Handler
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.BuildConfig
import com.app.parkfinder.foreground.NotificationService
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.NavigationStatus
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.NavigationStep
import com.app.parkfinder.logic.models.OsrmRouteResponse
import com.app.parkfinder.logic.models.Step
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotUpdateNotificationDto
import com.app.parkfinder.logic.services.MapService
import com.app.parkfinder.logic.services.NominatimService
import com.app.parkfinder.logic.services.OsrmService
import com.app.parkfinder.utilis.ParkingSpotStatusEnumDeserializer
import com.app.parkfinder.utilis.TaggedPolygon
import com.app.parkfinder.utilis.TextOverlay
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.microsoft.signalr.HubConnectionState
import kotlin.math.sqrt

class MapViewModel(application: Application) : AndroidViewModel(application), LocationListener {
    // ================ Variables ================
    private var hubConnection: HubConnection? = null

    private val locationManager = getApplication<Application>().getSystemService(LOCATION_SERVICE) as LocationManager
    private var steps: List<Step> = emptyList()
    private var instructions = mutableListOf<NavigationStep>()

    private val viewRadius: Int = 5     // in kilometers
    private val kmValue = 0.006         // 1km in degrees

    private var locationOverlay: MyLocationNewOverlay? = null

    private var selectedRoute: Polyline? = null
    private var selectedPoint: GeoPoint? = null
    private var navigatingToParkingSpotId = -1

    private var osrmRouteResponse: OsrmRouteResponse? = null
    private var currentParkingSpotOverlays: MutableList<Overlay> = mutableListOf()
    private var currentParkingLotOverlays: MutableList<Overlay> = mutableListOf()
    private var currentTextOverlays: MutableList<TextOverlay> = mutableListOf()
    private var currentParkingLotClickedId: Int = -1
    var clickedLot: ParkingLotDto? = null
    var clickedSpotNumber: String = ""
    private var shownParkingSpots: List<ParkingSpotDto> = emptyList()
    private var clickedGeoPoints = mutableListOf<GeoPoint>()

    // ================ Services ================
    private val osrmService = OsrmService.create()
    private val nominatimService = NominatimService.create()
    private val mapService = RetrofitConfig.createService(MapService::class.java)

    // ================ LiveData ================
    private val _getAllParkingLotsRes = MutableLiveData<BackResponse<List<ParkingLotDto>>?>()
    private val _getParkingSpotsForParkingLot = MutableLiveData<BackResponse<List<ParkingSpotDto>>>()
    private val _getParkingSpotsForParkingLotSearch = MutableLiveData<BackResponse<List<ParkingSpotDto>>>()
    private val _getAllParkingLotsAroundLocationRes = MutableLiveData<BackResponse<List<ParkingLotDto>>?>()
    private val _getAllInstructions = MutableLiveData<List<NavigationStep>>()
    private val _getParkingSpotUpdates = MutableLiveData<List<ParkingSpotUpdateNotificationDto>>()
    private val _parkingSpotClicked = MutableSharedFlow<ParkingSpotDto>()
    private val _showConfirmReservationModal = MutableLiveData<Unit?>()
    private val _currentNavigationStep = MutableLiveData<NavigationStep?>()
    private val _navigationActive = MutableLiveData<Boolean>()

    // ================ Exposed LiveData ================
    val getAllParkingLotsAroundLocationRes: MutableLiveData<BackResponse<List<ParkingLotDto>>?> = _getAllParkingLotsAroundLocationRes
    val getAllInstructions: LiveData<List<NavigationStep>> = _getAllInstructions
    val getParkingSpotUpdate: LiveData<List<ParkingSpotUpdateNotificationDto>> = _getParkingSpotUpdates
    val getParkingSpotsForParkingLotSearch: LiveData<BackResponse<List<ParkingSpotDto>>> = _getParkingSpotsForParkingLotSearch
    val parkingSpotClicked = _parkingSpotClicked.asSharedFlow()
    val showConfirmReservationModal: LiveData<Unit?> = _showConfirmReservationModal
    val currentNavigationStep: LiveData<NavigationStep?> = _currentNavigationStep
    val navigationActive: LiveData<Boolean> = _navigationActive

    // ================ Gson ================
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(ParkingSpotStatusEnum::class.java, ParkingSpotStatusEnumDeserializer())
        .create()

    // ================ Handlers ================
    private val connectionCheckHandler = Handler(Looper.getMainLooper())
    private val connectionCheckInterval: Long = 10000 // 10 seconds

    // ================ Companion Object ================
    // Static variables and functions
    companion object {
        var lastLocation: GeoPoint? = null
        var isMapView: Boolean = false
        var mapView: MapView? = null
        private const val KEY_NAVIGATING_TO_PARKING_SPOT = "navigating_to_parking_spot"

        fun getParkingSpotPoints(spot:ParkingSpotDto): MutableList<GeoPoint> {
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
            return geoPoints
        }

        fun calculateCentroid(points: List<GeoPoint>): GeoPoint {
            var centroidLat = 0.0
            var centroidLon = 0.0

            for (point in points) {
                centroidLat += point.latitude
                centroidLon += point.longitude
            }

            val totalPoints = points.size
            return GeoPoint(centroidLat / totalPoints, centroidLon / totalPoints)
        }
    }

    init {
        _getAllParkingLotsRes.observeForever { res ->
            if (res?.isSuccessful == true) {
                mapView?.let { drawParkingLots(it, res.data) }
            }
        }

        _getParkingSpotsForParkingLot.observeForever { res ->
            if (res.isSuccessful) {
                shownParkingSpots = res.data
                mapView?.let { drawParkingSpots(it, res.data) }
            }
        }

        startConnectionCheck()
    }

    private fun startConnectionCheck() {
        connectionCheckHandler.postDelayed(object : Runnable {
            override fun run() {
                if (hubConnection?.connectionState != HubConnectionState.CONNECTED) {
                    Log.i("MapViewModel", "Hub connection lost. Attempting to reconnect...")
                    hubConnection = null
                    startHubConnection()
                }
                connectionCheckHandler.postDelayed(this, connectionCheckInterval)
            }
        }, connectionCheckInterval)
    }

    private fun stopConnectionCheck() {
        connectionCheckHandler.removeCallbacksAndMessages(null)
    }

    private fun startHubConnection() {
        if (hubConnection == null) {
            try {
                // Create a new HubConnection
                hubConnection = HubConnectionBuilder.create(BuildConfig.BACKEND_URL + "baseHub")
                    .withTransport(com.microsoft.signalr.TransportEnum.LONG_POLLING)
                    .withAccessTokenProvider(Single.defer {
                        Single.just((AppPreferences.accessToken) ?: "")
                    })
                    .build()

                hubConnection?.on(
                    "ParkingSpotStatusUpdated",
                    { data ->
                        val type =
                            object : TypeToken<List<ParkingSpotUpdateNotificationDto>>() {}.type
                        val notifications: List<ParkingSpotUpdateNotificationDto> =
                            gson.fromJson(data.toString(), type)
                        _getParkingSpotUpdates.postValue(notifications)//post updates
                        for (notification in notifications) {
                            // Find the parking spot overlay
                            val parkingSpotOverlay = currentParkingSpotOverlays.find { it is TaggedPolygon && it.tag == notification.parkingSpotId.toString() }
                            if (parkingSpotOverlay != null) {
                                val polygon = parkingSpotOverlay as TaggedPolygon
                                when (notification.getParkingSpotStatusEnum()) {
                                    ParkingSpotStatusEnum.FREE -> {
                                        // Green
                                        polygon.fillPaint.color = Color.argb(100, 0, 255, 0)
                                        polygon.setOnClickListener { _, _, _ ->
                                            clickedSpotNumber =
                                                "P${currentParkingSpotOverlays.indexOf(polygon) + 1}"
                                            clickedGeoPoints = polygon.points

                                            val parkingSpotData =
                                                shownParkingSpots.find { it.id == notification.parkingSpotId }
                                            if (parkingSpotData != null) {
                                                viewModelScope.launch {
                                                    _parkingSpotClicked.emit(parkingSpotData)
                                                }
                                            }
                                            true
                                        }
                                    }

                                    ParkingSpotStatusEnum.RESERVED, ParkingSpotStatusEnum.OCCUPIED, ParkingSpotStatusEnum.OCCUPIED_BY_SIMULATION, ParkingSpotStatusEnum.TEMPORARILY_UNAVAILABLE -> {
                                        // Set appropriate color based on status
                                        polygon.fillPaint.color =
                                            when (notification.getParkingSpotStatusEnum()) {
                                                ParkingSpotStatusEnum.RESERVED -> Color.argb(
                                                    100,
                                                    255,
                                                    255,
                                                    0
                                                ) // Yellow
                                                ParkingSpotStatusEnum.OCCUPIED, ParkingSpotStatusEnum.OCCUPIED_BY_SIMULATION -> Color.argb(
                                                    100,
                                                    255,
                                                    0,
                                                    0
                                                ) // Red
                                                ParkingSpotStatusEnum.TEMPORARILY_UNAVAILABLE -> Color.argb(
                                                    100,
                                                    0,
                                                    0,
                                                    255
                                                ) // Blue
                                                else -> Color.argb(100, 128, 128, 128) // Gray
                                            }
                                        polygon.setOnClickListener { _, _, _ ->
                                            Toast.makeText(
                                                getApplication(),
                                                "Parking spot is not available for reservation",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            true
                                        }
                                    }
                                }
                                mapView?.invalidate()
                            }

                            // In case we are navigating to a parking spot and its status changes to unavailable
                            // stop the navigation and show a message to the user that the spot is no longer available
                            if (navigatingToParkingSpotId == notification.parkingSpotId &&
                                notification.getParkingSpotStatusEnum() != ParkingSpotStatusEnum.FREE
                            ) {
                                stopNavigation()
                            }
                        }
                    },
                    List::class.java
                )

                hubConnection?.on(
                    "ParkingLotCreated",
                    { data ->
                        val jsonObject = JsonParser.parseString(data.polygonGeoJson).asJsonObject
                        val coordinatesArray = jsonObject.getAsJsonObject("geometry")
                            .getAsJsonArray("coordinates")
                            .get(0) // gets the first one in the file

                        val geoPoints = mutableListOf<GeoPoint>()
                        for (coordinate in coordinatesArray.asJsonArray) {
                            val lng = coordinate.asJsonArray[0].asDouble
                            val lat = coordinate.asJsonArray[1].asDouble
                            geoPoints.add(GeoPoint(lat, lng))
                        }

                        if (mapView != null) {
                            val polygon = TaggedPolygon(mapView!!)
                            polygon.tag = data.id.toString() // Set the tag for the parking lot overlay

                            polygon.points = geoPoints
                            polygon.fillPaint.color = Color.argb(60, 0, 0, 255) // Semi-transparent blue
                            polygon.outlinePaint.color = Color.RED
                            polygon.outlinePaint.strokeWidth = 2f

                            polygon.setOnClickListener { _, _, _ ->
                                if (currentParkingLotClickedId != data.id) {
                                    currentParkingLotClickedId = data.id
                                    clickedLot = data
                                    getParkingSpotsForParkingLot(data.id)
                                } else {
                                    clearParkingSpotAndTextOverlays()
                                    shownParkingSpots = emptyList()
                                    currentParkingLotClickedId = -1
                                }

                                true
                            }

                            // Check the distance between the new parking lot and the current location
                            lastLocation?.let {
                                val distance = calculateDistance(
                                    GeoPoint(it.latitude, it.longitude),
                                    calculateCentroid(geoPoints)
                                )
                                if (distance <= viewRadius * kmValue * 111000) {
                                    mapView!!.overlays.add(polygon)
                                    mapView!!.invalidate()
                                }
                            }
                        }

                    },
                    ParkingLotDto::class.java
                )

                hubConnection?.on(
                    "ParkingLotsDeleted",
                    { data ->
                        // Iterate through the parking lots that were deleted
                        val type = object : TypeToken<List<Int>>() {}.type
                        val deletedParkingLotIds: List<Int> = gson.fromJson(data.toString(), type)

                        // Remove the parking lot overlays that were deleted
                        for (deletedParkingLotId in deletedParkingLotIds) {
                            val deletedOverlay = currentParkingLotOverlays.find { it is TaggedPolygon && it.tag == deletedParkingLotId.toString() }
                            if (deletedOverlay != null) {
                                mapView?.overlays?.remove(deletedOverlay)
                                currentParkingLotOverlays.remove(deletedOverlay)

                                // In case that the parking lot was clicked, clear the parking spot overlays
                                if (currentParkingLotClickedId == deletedParkingLotId) {
                                    clearParkingSpotAndTextOverlays()
                                    shownParkingSpots = emptyList()
                                    currentParkingLotClickedId = -1
                                }
                            }
                        }

                        mapView?.invalidate()   // Refresh the map
                    },
                    List::class.java
                )

                // Start the connection
                hubConnection?.start()?.blockingAwait()
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error starting hub connection", e)
//                if (e.message?.contains("Unauthorized") == true) {
//                    Toast.makeText(getApplication(), "Unauthorized access. Please log in again.", Toast.LENGTH_SHORT).show()
//                    // Handle unauthorized access, e.g., redirect to login screen
//                } else {
//                    Toast.makeText(getApplication(), "Error starting hub connection: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }

    private fun stopHubConnection() {
        hubConnection?.stop()
        hubConnection = null
    }

    override fun onCleared() {
        stopConnectionCheck()
        stopHubConnection()
        stopLocationTrack()
        super.onCleared()
    }

    fun initializeMap(mapView: MapView) {
        MapViewModel.mapView = mapView
        val locationProvider = GpsMyLocationProvider(getApplication())
        locationOverlay = MyLocationNewOverlay(locationProvider, mapView).apply {
            enableMyLocation()
            runOnFirstFix {
                myLocation?.let {
                    val initialLocation = GeoPoint(it.latitude, it.longitude)
                    lastLocation = initialLocation
                    mapView.controller.setCenter(initialLocation)
                    getNearbyParkingLots(it.latitude, it.longitude, viewRadius)
                    drawCircle(mapView, it.latitude, it.longitude)
                }
            }
        }
        mapView.overlays.add(locationOverlay)

        // Disable zoom buttons
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.zoomController.setZoomInEnabled(false)
        mapView.zoomController.setZoomOutEnabled(false)
        startLocationTrack()
        startHubConnection()
    }

    private suspend fun getLocationCoordinates(location: String): GeoPoint? {
        return try {
            val response = withContext(Dispatchers.IO) {
                nominatimService.getCoordinates(location, "json", 1, 1, "sparkParkFinder").execute()
            }
            if (response.isSuccessful) {
                val result = response.body()?.firstOrNull()
                val lat = result?.lat?.toDoubleOrNull()
                val lon = result?.lon?.toDoubleOrNull()
                if (lat != null && lon != null) GeoPoint(lat, lon) else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun searchByLocation(location: String, radius: Int) {
        viewModelScope.launch {
            val point = getLocationCoordinates(location)
            if (point == null)
                _getAllParkingLotsAroundLocationRes.postValue(null)
            else {
                getNearbyParkingLots(point.latitude, point.longitude, radius, true)
            }

        }
    }

    fun getParkingLotSpotsSearch(lotId:Int) {
        viewModelScope.launch {
            getParkingSpotsForParkingLot(lotId,true)
        }
    }

    private fun getNearbyParkingLots(lat: Double, long: Double, radius: Int, isSearch: Boolean = false) {
        viewModelScope.launch {
            try {
                val response = mapService.GetAllNearbyParkingLots(radius * kmValue, lat, long)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (isSearch)
                            _getAllParkingLotsAroundLocationRes.postValue(it)
                        else
                            _getAllParkingLotsRes.postValue(it)
                    }
                } else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<ParkingLotDto>()
                    ).let {
                        if (isSearch)
                            _getAllParkingLotsAroundLocationRes.postValue(it)
                        else
                            _getAllParkingLotsRes.postValue(it)
                    }
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = emptyList<ParkingLotDto>()
                ).let {
                    if (isSearch)
                        _getAllParkingLotsAroundLocationRes.postValue(it)
                    else
                        _getAllParkingLotsRes.postValue(it)
                }
            }
        }
    }

    private fun getParkingSpotsForParkingLot(parkingLotId: Int, isSearch: Boolean = false) {
        viewModelScope.launch {
            try {
                val response = mapService.getParkingSpotsForParkingLot(parkingLotId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        if(isSearch)
                            _getParkingSpotsForParkingLotSearch.postValue(it)
                        else
                            _getParkingSpotsForParkingLot.postValue(it)
                    }
                } else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<ParkingSpotDto>()
                    ).let {
                        if(isSearch)
                            _getParkingSpotsForParkingLotSearch.postValue(it)
                        else
                            _getParkingSpotsForParkingLot.postValue(it)
                    }
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = emptyList<ParkingSpotDto>()
                ).let {
                    if(isSearch)
                        _getParkingSpotsForParkingLotSearch.postValue(it)
                    else
                        _getParkingSpotsForParkingLot.postValue(it)
                }
            }
        }
    }

    // Function to fetch and draw route between start and end points
    private suspend fun drawRoute(mapView: MapView, start: GeoPoint, end: GeoPoint): Polyline? {
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
                    steps.map { step: Step ->
                        val direction =
                            " " + step.maneuver.modifier // Add space before modifier if present
                        val instruction: String = when (step.maneuver.type) {
                            "turn" -> "Turn$direction onto ${step.name}."
                            "roundabout" -> {
                                val exitText =
                                    step.maneuver.exit?.let { " and take the $it exit" } ?: ""
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
                    _currentNavigationStep.postValue(instructions.get(1))

                    mapView.overlays.remove(selectedRoute)
                    mapView.overlays.add(polyline)

                    polyline.setOnClickListener { _, _, _ ->
                        mapView.overlays.remove(selectedRoute)
                        instructions.clear()
                        _getAllInstructions.postValue(instructions)
                        selectedRoute = null
                        selectedPoint = null
                        navigatingToParkingSpotId = -1
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
        val newParkingLotOverlays = mutableListOf<Overlay>()

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

            val polygon = TaggedPolygon(mapView)
            polygon.tag = lot.id.toString() // Set the tag for the parking lot overlay

            polygon.points = geoPoints
            polygon.fillPaint.color = Color.argb(60, 0, 0, 255) // Semi-transparent blue
            polygon.outlinePaint.color = Color.RED
            polygon.outlinePaint.strokeWidth = 2f


            polygon.setOnClickListener { _, _, _ ->
                if (currentParkingLotClickedId != lot.id) {
                    currentParkingLotClickedId = lot.id
                    clickedLot = lot
                    getParkingSpotsForParkingLot(lot.id)
                } else {
                    clearParkingSpotAndTextOverlays()
                    shownParkingSpots = emptyList()
                    currentParkingLotClickedId = -1
                }

                true
            }

            // Add the Polygon to the MapView
            newParkingLotOverlays.add(polygon)
        }

        // Clear parking lot overlays that are not in the new list
        for (overlay in currentParkingLotOverlays) {
            if (!newParkingLotOverlays.contains(overlay)) {
                mapView.overlays.remove(overlay)
            }
        }

        // Add new parking lot overlays that are not in the current list
        for (overlay in newParkingLotOverlays) {
            if (!currentParkingLotOverlays.contains(overlay)) {
                mapView.overlays.add(overlay)
            }
        }

        currentParkingLotOverlays = newParkingLotOverlays

        // Re-add parking spot overlays to ensure they are on top
        if (currentParkingSpotOverlays.isNotEmpty()) {
            mapView.overlays.removeAll(currentParkingSpotOverlays)
            mapView.overlays.addAll(currentParkingSpotOverlays)
        }

        mapView.invalidate() // Refresh the map
    }

    private fun drawParkingSpots(mapView: MapView, pLots: List<ParkingSpotDto>) {
        clearParkingSpotAndTextOverlays()

        var spotNumber = 1
        for (spot in pLots) {
            val geoPoints = getParkingSpotPoints(spot)
            val polygon = TaggedPolygon(mapView)
            polygon.tag = spot.id.toString() // Set the tag for the parking spot overlay
            polygon.points = geoPoints

            when (spot.parkingSpotStatus) {
                ParkingSpotStatusEnum.FREE.ordinal -> {
                    // Green
                    polygon.fillPaint.color = Color.argb(100, 0, 255, 0)
                }

                ParkingSpotStatusEnum.RESERVED.ordinal -> {
                    // Yellow
                    polygon.fillPaint.color = Color.argb(100, 255, 255, 0)
                }

                ParkingSpotStatusEnum.OCCUPIED.ordinal -> {
                    // Red
                    polygon.fillPaint.color = Color.argb(100, 255, 0, 0)
                }

                ParkingSpotStatusEnum.OCCUPIED_BY_SIMULATION.ordinal -> {
                    // Red
                    polygon.fillPaint.color = Color.argb(100, 255, 0, 0)
                }

                ParkingSpotStatusEnum.TEMPORARILY_UNAVAILABLE.ordinal -> {
                    // Blue
                    polygon.fillPaint.color = Color.argb(100, 0, 0, 255)
                }

                else -> {
                    // Gray
                    polygon.fillPaint.color = Color.argb(100, 128, 128, 128)
                }
            }

            polygon.outlinePaint.color = Color.RED
            polygon.outlinePaint.strokeWidth = 2f

            polygon.setOnClickListener { _, _, _ ->
                when (spot.parkingSpotStatus) {
                    ParkingSpotStatusEnum.FREE.ordinal -> {

                    clickedSpotNumber = "P${pLots.indexOf(spot) + 1}"
                    clickedGeoPoints = geoPoints

                        viewModelScope.launch {
                            _parkingSpotClicked.emit(spot)
                        }
                    }

                    ParkingSpotStatusEnum.OCCUPIED.ordinal -> {
                        Toast.makeText(
                            getApplication(),
                            "Parking spot is not available for reservation",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ParkingSpotStatusEnum.RESERVED.ordinal -> {
                        Toast.makeText(
                            getApplication(),
                            "Parking spot is not available for reservation",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Toast.makeText(
                            getApplication(),
                            "Parking spot is not available for reservation",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                true
            }

            currentParkingSpotOverlays.add(polygon)

            // Add text overlay with spot number
            val textOverlay = TextOverlay(
                text = "P$spotNumber",
                position = calculateCentroid(geoPoints),
                textPaint = Paint(
                    Paint.ANTI_ALIAS_FLAG
                )
            )
            textOverlay.textPaint.color = Color.BLACK
            textOverlay.textPaint.textSize = 30f

            currentTextOverlays.add(textOverlay)

            spotNumber++
        }

        mapView.overlays.addAll(currentParkingSpotOverlays)
        mapView.overlays.addAll(currentTextOverlays)

        mapView.invalidate() // Refresh the map
    }

    private fun clearParkingSpotAndTextOverlays() {
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
    }

    private fun drawCircle(mapView: MapView, latitude: Double, longitude: Double) {

        // Remove the previous circle overlay if it exists
        val previousCircle =
            mapView.overlays.find { it is TaggedPolygon && it.tag == "location_circle" }
        if (previousCircle != null) {
            mapView.overlays.remove(previousCircle)
        }

        val circle = TaggedPolygon(mapView)
        circle.tag = "location_circle" // Set the tag for the circle overlay

        val points = mutableListOf<GeoPoint>()
        val numPoints = 100
        val radiusInMeters = this.viewRadius * this.kmValue * 111000 // Convert degrees to meters

        for (i in 0 until numPoints) {
            val angle = 2 * Math.PI * i / numPoints
            val dx = radiusInMeters * cos(angle)
            val dy = radiusInMeters * sin(angle)
            val point = GeoPoint(
                latitude + (dy / 111000),
                longitude + (dx / (111000 * cos(Math.toRadians(latitude))))
            )
            points.add(point)
        }

        circle.points = points
        circle.fillPaint.color = Color.argb(90, 190, 228, 235) // Semi-transparent light blue
        circle.outlinePaint.color = Color.RED
        circle.outlinePaint.strokeWidth = 2f

        // Disable default click behavior
        circle.setOnClickListener { _, _, _ -> true }

        mapView.overlays?.add(circle)
        mapView.invalidate() // Refresh the map
    }

    //draws route from current location to parking spot
    fun startNavigation(spot: ParkingSpotDto) {
        viewModelScope.launch {
            if (selectedRoute != null)
                mapView?.overlays?.remove(selectedRoute)
            clickedGeoPoints = getParkingSpotPoints(spot)
            selectedPoint = calculateCentroid(clickedGeoPoints)
            navigatingToParkingSpotId = spot.id
            _navigationActive.postValue(true)

            setCenterToMyLocation()

            selectedRoute = mapView?.let { drawRoute(it, lastLocation!!, selectedPoint!!) }
            lastLocation?.let { selectedPoint?.let { it1 -> checkIfArrived(it, it1) } }
        }
    }

    fun stopNavigation(){
        selectedPoint = null
        navigatingToParkingSpotId = -1
        mapView?.overlays?.remove(selectedRoute)
        selectedRoute = null
        clickedGeoPoints = emptyList<GeoPoint>().toMutableList()
        setCenterToMyLocation()

        _navigationActive.postValue(false)
        _currentNavigationStep.postValue(null)
        NavigationStatus.isParkingSpotReserved.postValue(null)
        NavigationStatus.isSpotSelected.postValue(null)

        mapView?.invalidate()
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

    fun resetShowModalSignal(){
        _showConfirmReservationModal.postValue(null)
    }

    fun destroy() {
        locationOverlay?.disableMyLocation()
        stopHubConnection()
        super.onCleared()
    }

    private fun stopLocationTrack() {
        locationManager.removeUpdates(this)
    }

    private fun startLocationTrack() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L,
                10f,
                this
            )
        } catch (e: SecurityException) {
            Log.e("monkey", "Location permission required")
        }
    }

    private fun checkIfArrived(currentLocation: GeoPoint, destination: GeoPoint, threshold: Double = 50.0){
        val distance = calculateDistance(currentLocation, destination)
        if (distance <= threshold) {
            _showConfirmReservationModal.postValue(Unit)
        }
    }

    private fun calculateDistance(from: GeoPoint, to: GeoPoint): Double {
        val lat1 = Math.toRadians(from.latitude)
        val lon1 = Math.toRadians(from.longitude)
        val lat2 = Math.toRadians(to.latitude)
        val lon2 = Math.toRadians(to.longitude)

        val r = 6371e3

        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1

        val a = sin(deltaLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(deltaLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }

    override fun onLocationChanged(loc: Location) {
        val newLocation = GeoPoint(loc.latitude, loc.longitude)

        lastLocation = newLocation
        NotificationService.userLocation = newLocation

        selectedPoint?.let { it1 -> checkIfArrived(newLocation, it1) }
        if(isMapView) {
            mapView?.let { mapView ->
                if (selectedRoute != null) {
                    mapView.controller.setCenter(newLocation)
                }

                // Update location overlay
                locationOverlay?.let {
                    if (!mapView.overlays.contains(it)) {
                        mapView.overlays.add(it)
                    }
                }

                // Update nearby parking lots
                getNearbyParkingLots(loc.latitude, loc.longitude, viewRadius)

                // Draw new circle around the new location
                drawCircle(mapView, loc.latitude, loc.longitude)

                // Update route if necessary
                viewModelScope.launch {
                    if (selectedRoute != null) {
                        mapView.overlays.remove(selectedRoute)
                    }
                    if (selectedPoint != null) {
                        selectedRoute = drawRoute(mapView, newLocation, selectedPoint!!)
                    }
                }

                // Check if current parking lot clicked is still in view (e.g., if user moves away)
                if (
                    currentParkingLotClickedId != -1 &&
                    mapView.overlays.contains(currentParkingLotOverlays.find { it is TaggedPolygon && it.tag == currentParkingLotClickedId.toString() })
                ) {
                    // Ensure current parking spot overlays are added
                    if (currentParkingSpotOverlays.isNotEmpty()) {
                        for (overlay in currentParkingSpotOverlays) {
                            if (!mapView.overlays.contains(overlay)) {
                                mapView.overlays.add(overlay)
                            }
                        }
                    }

                    // Ensure current text overlays are added
                    if (currentTextOverlays.isNotEmpty()) {
                        for (overlay in currentTextOverlays) {
                            if (!mapView.overlays.contains(overlay)) {
                                mapView.overlays.add(overlay)
                            }
                        }
                    }

                } else {  // Clear parking spot overlays if the parking lot is not in view
                    clearParkingSpotAndTextOverlays()
                    shownParkingSpots = emptyList()
                    currentParkingLotClickedId = -1
                }

                mapView.invalidate() // Refresh the map
            }
        }
    }
}