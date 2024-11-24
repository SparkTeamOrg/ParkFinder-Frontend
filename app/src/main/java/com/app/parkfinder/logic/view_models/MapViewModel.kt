package com.app.parkfinder.logic.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.services.MapService
import com.app.parkfinder.logic.services.OsrmService
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
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

    private val viewRadius: Double = 0.03 // User can see parking lots within radius of 0.03 degrees

    private var locationOverlay: MyLocationNewOverlay? = null
    private var lastLocation: GeoPoint? = null

    private var selectedRoute: Polyline? = null
    private var selectedPoint: GeoPoint? = null

    private val osrmService = OsrmService.create()
    private val mapService = RetrofitConfig.createService(MapService::class.java)
    private val _getAllParkingLotsRes = MutableLiveData<BackResponse<List<ParkingLotDto>>>()

    val getAllParkingLotsRes: LiveData<BackResponse<List<ParkingLotDto>>> = _getAllParkingLotsRes

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

        // Disable zoom buttons
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.zoomController.setZoomInEnabled(false)
        mapView.zoomController.setZoomOutEnabled(false)

        // Set location change listener
        locationProvider.startLocationProvider(this)
    }

    override fun onLocationChanged(location: Location?, source: IMyLocationProvider?) {
        location?.let {
            val newLocation = GeoPoint(it.latitude, it.longitude)
            Logger.getLogger("MapViewModel").info("Location changed to: $newLocation")
            if (lastLocation == null || newLocation.distanceToAsDouble(lastLocation) > 10) {
                lastLocation = newLocation
                mapView?.controller?.setCenter(newLocation)
                getNearbyParkingLots(it.latitude, it.longitude)
                drawCircle(it.latitude, it.longitude)

                viewModelScope.launch {
                    if(selectedRoute!=null)
                        mapView?.overlays?.remove(selectedRoute)
                    selectedRoute = drawRoute(mapView!!,newLocation,selectedPoint!!);
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

    // Function to fetch and draw route between start and end points
    private suspend fun drawRoute(mapView: MapView, start: GeoPoint, end: GeoPoint) :Polyline? {
        val startCoords = "${start.longitude},${start.latitude}"
        val endCoords = "${end.longitude},${end.latitude}"

        try {
            val response = withContext(Dispatchers.IO) {
                osrmService.getRoute(startCoords, endCoords).execute()
            }

            if (response.isSuccessful) {
                val routeResponse = response.body()
                val geometry = routeResponse?.routes?.firstOrNull()?.geometry
                geometry?.let {
                    val geoPoints = it.coordinates.map { coord ->
                        GeoPoint(coord[1], coord[0])
                    }
                    val polyline = Polyline().apply {
                        setPoints(geoPoints)
                        outlinePaint.color = Color.rgb(0, 0, 255)
                    }
                    mapView.overlays.removeIf { overlay -> overlay is Polyline }
                    mapView.overlays.add(polyline)
                    polyline.setOnClickListener{_,_,_->
                        mapView.overlays.remove(selectedRoute)
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
}