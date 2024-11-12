package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.parkfinder.logic.models.dtos.UserDto
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun HomeScreen(
    user: UserDto
) {
    var mapView: MapView? = null
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(
//            text = "Welcome ${user.Fullname} - You are logged in",
//            color = Color.White
//        )

        AndroidView(factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
//                controller.setCenter(GeoPoint(44.0165, 21.0059))  // Center in Serbia
//                controller.setZoom(10.0)
                // Define bounding box for Serbia
                val serbiaBounds = BoundingBox(
                    46.18,   // North
                    23.0,   // West
                    41.85,   // South
                    18.83     // East
                )

                // Set boundary limits and center on Serbia
                setScrollableAreaLimitDouble(serbiaBounds)
                controller.setZoom(7.5)
                controller.setCenter(GeoPoint(44.0, 20.5)) // Center near Belgrade, Serbia
                minZoomLevel = 8.0
                mapView = this

            }
        }, update = {
            mapView = it
        })
    }
}
