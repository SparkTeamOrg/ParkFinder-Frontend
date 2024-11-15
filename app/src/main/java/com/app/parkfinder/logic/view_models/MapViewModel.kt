package com.app.parkfinder.logic.view_models

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    var mapView: MapView? = null
    private var locationOverlay: MyLocationNewOverlay? = null

    fun initializeMap(mapView: MapView) {
        this.mapView = mapView
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(getApplication()), mapView).apply {
            enableMyLocation()
            runOnFirstFix {
                val myLocation: GeoPoint? = myLocation
                myLocation?.let {
                    mapView.controller.setCenter(GeoPoint(it.latitude, it.longitude))
                }
            }
        }
        mapView.overlays.add(locationOverlay)

        // Disable zoom buttons
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.zoomController.setZoomInEnabled(false)
        mapView.zoomController.setZoomOutEnabled(false)
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