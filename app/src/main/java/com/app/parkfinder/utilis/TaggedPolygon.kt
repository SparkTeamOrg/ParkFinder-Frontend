package com.app.parkfinder.utilis

import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class TaggedPolygon(mapView: MapView) : Polygon(mapView) {
    var tag: String? = null
}