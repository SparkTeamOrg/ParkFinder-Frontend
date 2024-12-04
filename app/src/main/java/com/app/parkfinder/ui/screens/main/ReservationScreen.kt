package com.app.parkfinder.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.VehicleDto

@SuppressLint("DefaultLocale")
@Composable
fun ReservationScreen(
    lot: ParkingLotDto,
    spotNumber: String,
    startNavigation: (String) -> Unit,
    vehicles: List<VehicleDto>,
    selectedVehicle: Int,
    onSelectedVehicleChange: (Int)->Unit
) {
    val status = if (lot.occupied == 0) "Free for reservation" else "Already reserved"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.parking_space),  // Replace with your image resource
                contentDescription = "Parking Spot",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            Text(
                text = "Space details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-50).dp)
                .background(Color(0xFF151A24), shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .width(325.dp)
                            .height(65.dp)
                    ) {
                        Text(
                            text = (
                                    if (lot.road != null)
                                        lot.road + " " + lot.city
                                    else "Unknown road " + lot.city
                            ),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            modifier = Modifier.width(325.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Text(
                                text =  String.format("%.2f", lot.distance) + " km",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(start = 15.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .background(
                                Color(0xFF333333),
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .clickable { /* Handle click here */ }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Bookmark",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Spot number",
                    fontSize = 18.sp,
                    color = Color.White,
                )
                Text(
                    text = spotNumber,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF00AEEF)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Status",
                    fontSize = 18.sp,
                    color = Color.White,
                )
                Text(
                    text = status,
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = if (lot.occupied == 0) Color(0xFF00AEEF) else Color.Red
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Choose your vehicle",
                    fontSize = 18.sp,
                    color = Color.White,
                )
                OutlinedDropdownMenu(
                    label = "",
                    selectedText = "Select a vehicle",
                    options = vehicles,
                    icon = Icons.Default.DirectionsCar,
                    onOptionSelected = { option -> onSelectedVehicleChange(option) }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { startNavigation(spotNumber) },
                        modifier = Modifier
                            .width(220.dp)
                            .height(48.dp)
                            .align(Alignment.Center),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00AEEF),
                            disabledBackgroundColor = Color(0xFF0FCFFF).copy(alpha = 0.3f),
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = selectedVehicle != 0,
                    ) {
                        Text(
                            text = "Reserve and navigate",
                            color = if(selectedVehicle!=0) White else White.copy(alpha = 0.3f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDropdownMenu(
    label: String,
    selectedText: String,
    options: List<VehicleDto>,
    icon: ImageVector,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(selectedText) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = showText,
            onValueChange = {},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(36, 45, 64),
                unfocusedBorderColor = White,
                unfocusedTextColor = White,
                focusedTextColor = White,
                errorTextColor = Color.Red
            ),
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = "Car Icon",
                    tint = White
                )
            },
            trailingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = White
                )
            },
            label = { Text(label, color = White) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(36, 45, 64))
        ) {
            options.forEach { opt ->
                val vehicleInfo = opt.vehicleModelVehicleBrandName + " " + opt.vehicleModelName + " " + opt.licencePlate
                DropdownMenuItem(
                    text = { Text(vehicleInfo, color = White) },
                    onClick = {
                        expanded = false
                        onOptionSelected(opt.id)
                        showText = vehicleInfo
                    },
                    modifier = Modifier.background(Color(36, 45, 64))
                )
            }
        }
    }
}