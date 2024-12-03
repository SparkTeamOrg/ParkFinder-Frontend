package com.app.parkfinder.utilis

import android.graphics.Canvas
import android.graphics.Paint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class TextOverlay (
    var text: String,
    var position: GeoPoint,
    var textPaint: Paint
) : Overlay() {

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (shadow) return

        val screenPoint = mapView.projection.toPixels(position, null)
        canvas.drawText(text, screenPoint.x.toFloat(), screenPoint.y.toFloat(), textPaint)
    }

}