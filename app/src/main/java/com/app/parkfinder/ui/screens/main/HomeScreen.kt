package com.app.parkfinder.ui.screens.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.logic.NavigationStatus
import com.app.parkfinder.logic.models.NavigationStep
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.composables.DirectionsPanel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView

@SuppressLint("OpaqueUnitKey")
@Composable
fun HomeScreen(
    user: UserDto,
    navigateToReservation: (ParkingSpotDto, ParkingLotDto, String) -> Unit,
    confirmReservation: (Int) -> Unit,
    cancelReservation: (Int) -> Unit,
    mapViewModel: MapViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isSidebarVisible by remember { mutableStateOf(false) }
    var steps = mutableListOf<NavigationStep>()
    var showModal by remember { mutableStateOf(false) }
    var showCancelButton by remember { mutableStateOf(false) }

    mapViewModel.getAllInstructions.observe(lifecycleOwner) { instructions ->
        steps = instructions.toMutableList()
    }

    mapViewModel.navigationActive.observe(lifecycleOwner) { active ->
        if (!active) {
            // Reset the sidebar visibility when navigation is over
            isSidebarVisible = false

            // Reset the instructions
            steps.clear()
        }
    }

    val currentStep by mapViewModel.currentNavigationStep.observeAsState()

    val show by mapViewModel.showConfirmReservationModal.observeAsState()
    LaunchedEffect(show) {
        if (show != null) {
            showModal = true
            mapViewModel.resetShowModalSignal()
            showCancelButton = false
        }
    }

    val reservationId by NavigationStatus.isParkingSpotReserved.observeAsState(null)
    val spot by NavigationStatus.isSpotSelected.observeAsState(null)
    LaunchedEffect(reservationId) {
        if(reservationId != null && spot != null){
            mapViewModel.startNavigation(spot!!)
            showCancelButton = true
        }
    }

    LaunchedEffect(mapViewModel.parkingSpotClicked) {
        mapViewModel.parkingSpotClicked.collect { spot ->
            mapViewModel.clickedLot?.let {
                navigateToReservation(spot, it, mapViewModel.clickedSpotNumber)
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                mapViewModel.enableMyLocation()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            mapViewModel.enableMyLocation()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
    ) {
        AndroidView(factory = { ctx ->
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", 0))
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                val serbiaBounds = BoundingBox(
                    46.18,   // North
                    23.0,   // West
                    41.85,   // South
                    18.83     // East
                )

                // Set boundary limits and center on Serbia
                setScrollableAreaLimitDouble(serbiaBounds)
                controller.setZoom(20)
                minZoomLevel = 8.0

                mapViewModel.initializeMap(this)
            }
        },
            update = {

            })

        // Display the current navigation step at the top of the screen
        currentStep?.let { step ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = step.instruction + " in " + step.distance + " meters" + " (" + step.duration + " seconds)",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Sidebar
        AnimatedVisibility(
            visible = isSidebarVisible,
            enter = slideInHorizontally { it }, // Slide in from the right
            exit = slideOutHorizontally { it } // Slide out to the right
        ) {
            DirectionsPanel(
                steps = steps,
                modifier = Modifier
                    .width(350.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF151A24))
                    .padding(16.dp)
                    .align(Alignment.CenterStart)
            )
        }

        if(showCancelButton) {
            // Toggle Sidebar Button
            FloatingActionButton(
                onClick = { isSidebarVisible = !isSidebarVisible },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isSidebarVisible) Icons.Default.Close else Icons.Default.Menu,
                    contentDescription = "Toggle Sidebar"
                )
            }
        }
    }

    if(showCancelButton) {
        Button(
            onClick = {
                reservationId?.let { cancelReservation(it) }
                mapViewModel.stopNavigation()
                showCancelButton = false
            },
            modifier = Modifier
                .padding(160.dp, 16.dp, 0.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Cancel",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(end = 16.dp, top = 16.dp, start = 5.dp)
    ) {

        // Button to zoom in
        Button(
            onClick = { mapViewModel.zoomIn() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            border = null,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to zoom out
        Button(
            onClick = { mapViewModel.zoomOut() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            border = null,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button to return to the current location
        Button(
            onClick = { mapViewModel.setCenterToMyLocation() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            border = null,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }

    ConfirmModal(showModal,
        onDismiss = { showModal = false },
        confirmReservation,
        cancelReservation,
        reservationId,
        mapViewModel
    )
}

@Composable
fun ConfirmModal(
    show: Boolean,
    onDismiss: () -> Unit,
    confirm: (Int) -> Unit,
    cancel: (Int) -> Unit,
    reservationId: Int?,
    mapViewModel: MapViewModel,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Confirm Reservation",
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "You have arrived at your destination. Please confirm your reservation to proceed.",
                        color = White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(
                        text = "By clicking confirm parking fee will start being charged on an hourly basis from now on.",
                        color = White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            },
            containerColor = Color(0xFF151A24).copy(alpha = 0.8f),
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        if (reservationId != null) {
                            confirm(reservationId)
                            mapViewModel.stopNavigation()
                        }
                        onDismiss()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF)
                    )
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        if (reservationId != null) {
                            cancel(reservationId)
                            mapViewModel.stopNavigation()
                        }
                        onDismiss()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}