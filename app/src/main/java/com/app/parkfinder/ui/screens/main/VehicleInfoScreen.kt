package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.VehicleDto
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip

@Composable
fun VehicleInfoScreen(
    vehicles: List<VehicleDto>,
    onBackClick: () -> Unit,
    onPlusClick: () -> Unit,
    onCanClick: (Int) -> Unit,
    onPenClick: (VehicleDto) -> Unit
) {
    var currentVehicle by remember { mutableStateOf(VehicleDto(-1, "", "", -1, "", -1, "")) }
    val pagerState = rememberPagerState(pageCount = { vehicles.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF293038), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.park_finder_logo),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Dummy",
                tint = Color.Transparent,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(0.25f))
            Text(
                text = "My Vehicles",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onPlusClick() },
                modifier = Modifier
                    .size(52.dp)
                    .background(Color(0xFF0FCFFF), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Vehicle",
                    tint = White
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val vehicle = vehicles[page]
            currentVehicle = vehicle

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.car),
                        contentDescription = "Background Image",
                        modifier = Modifier
                            .width(160.dp)
                            .height(160.dp),
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                VehicleLabel("Vehicle Company", vehicle.vehicleModelVehicleBrandName)
                VehicleLabel("VehicleModel", vehicle.vehicleModelName)
                VehicleLabel("Registration number", vehicle.licencePlate)
                VehicleLabel("Color", vehicle.color)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onPenClick(currentVehicle) },
                modifier = Modifier
                    .size(55.dp)
                    .background(Color(0xFF0FCFFF), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Update Vehicle",
                    tint = White
                )
            }
            if(vehicles.size > 1) {
                Spacer(modifier = Modifier.width(20.dp))
                DeleteButton(currentVehicle.id, onCanClick)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun VehicleLabel(label: String, value: String) {
    Text(
        text = label,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = White,
        modifier = Modifier
            .padding(bottom = 8.dp)
    )
    Divider(
        color = White,
        thickness = 2.dp,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = value,
        fontSize = 18.sp,
        color = Color.Gray,
    )
    Spacer(modifier = Modifier.height(22.dp))
}

@Composable
fun DeleteButton(vehicleId: Int, onCanClick: (Int) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    val isVehicleDeleted = remember { mutableStateOf(false) }
    IconButton(
        onClick = { showDialog.value = true },
        modifier = Modifier
            .size(55.dp)
            .background(Color.Red, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Vehicle",
            tint = White
        )
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false }, // Dismiss on outside click
            title = {
                Text(
                    text = "Confirm Deletion",
                    color = White
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this vehicle?",
                    color = White,
                    fontSize = 16.sp
                )
           },
            containerColor = Color(0xFF151A24),
            confirmButton = {
                Button(
                    onClick = {
                        onCanClick(vehicleId)
                        isVehicleDeleted.value = true
                        showDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Yes, Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF)
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}