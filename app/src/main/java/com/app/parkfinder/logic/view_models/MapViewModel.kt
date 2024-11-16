package com.app.parkfinder.logic.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.services.MapService
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    var mapView: MapView? = null


    private val viewRadius: Double = 0.01 // user view range




    private var locationOverlay: MyLocationNewOverlay? = null

    private val mapService = RetrofitConfig.createService(MapService::class.java)
    private val _getAllParkingLotsRes = MutableLiveData<BackResponse<List<ParkingLotDto>>>()

    val getAllParkingLotsRes: LiveData<BackResponse<List<ParkingLotDto>>> = _getAllParkingLotsRes

    fun initializeMap(mapView: MapView) {
        this.mapView = mapView
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(getApplication()), mapView).apply {
            enableMyLocation()
            runOnFirstFix {
                val myLocation: GeoPoint? = myLocation
                myLocation?.let {
                    mapView.controller.setCenter(GeoPoint(it.latitude, it.longitude))

                    getNearbyParkingLots(it.latitude,it.longitude)
                }
            }
        }
        mapView.overlays.add(locationOverlay)

        // Disable zoom buttons
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.zoomController.setZoomInEnabled(false)
        mapView.zoomController.setZoomOutEnabled(false)
    }

    private fun getNearbyParkingLots(lat:Double, long:Double)
    {
        viewModelScope.launch {
            try {
                val response = mapService.GetAllNearbyParkingLots(viewRadius,lat,long)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _getAllParkingLotsRes.postValue(it)
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = emptyList<ParkingLotDto>()
                    ).let {
                        _getAllParkingLotsRes.postValue(
                            it
                        )
                    }
                }

                // Note that all responses will be a successful response.
                // However, the response's isSuccessful parameter can be false
                // even if the response code is 200 OK
                response.body()?.let {
                    _getAllParkingLotsRes.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = emptyList<ParkingLotDto>()
                ).let {
                    _getAllParkingLotsRes.postValue(
                        it
                    )
                }
            }
        }
    }
    fun drawParkingLots(mapView: MapView, pLots: List<ParkingLotDto>) {

        for(lot in pLots) {
            val jsonObject = JsonParser.parseString(lot.polygonGeoJson).asJsonObject
            val coordinatesArray = jsonObject.getAsJsonObject("geometry")
                .getAsJsonArray("coordinates")
                .get(0)// gets the first one in the file

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

            // Add the Polygon to the MapView
            mapView.overlays.add(polygon)
        }
        mapView.invalidate() // Refresh the map
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
}