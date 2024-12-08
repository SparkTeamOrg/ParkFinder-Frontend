package com.app.parkfinder.ui.screens.parking

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.ui.composables.ParkFinderLogo

@SuppressLint("DefaultLocale")
@Composable
fun ParkingListScreen(
    parkingSpaces: List<ParkingLotDto>,
    navigateToParkingSpots: (lot:ParkingLotDto,name:String)->Unit = { _: ParkingLotDto, _: String -> }
) {
    Scaffold(
        topBar = {
            ParkFinderLogo()
        },
        content = { PaddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E))
                    .padding(PaddingValues)
            ) {
                items(parkingSpaces.size) { index ->
                    val roundedValue = String.format("%.2f", parkingSpaces[index].distance).toDouble()
                    parkingSpaces[index].distance = roundedValue
                    ParkingItem(parkingSpace = parkingSpaces[index], navigateToSpots = navigateToParkingSpots)
                }
            }
        }
    )
}

@Composable
fun ParkingItem(
    parkingSpace: ParkingLotDto,
    navigateToSpots: (lot:ParkingLotDto,name:String)->Unit = { _: ParkingLotDto, _: String -> }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2A2A))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon or Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF0077FF), shape = RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Text(
                    text = "P",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if(parkingSpace.town==null) parkingSpace.city +", " + parkingSpace.road else parkingSpace.town +", " + parkingSpace.road,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = "${parkingSpace.distance} km away",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "⭐ ${parkingSpace.rating}",
                    fontSize = 14.sp,
                    color = Color.Yellow
                )
                Button(
                    onClick = { navigateToSpots(parkingSpace,if(parkingSpace.town==null) parkingSpace.city +", " + parkingSpace.road else parkingSpace.town +", " + parkingSpace.road)},
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0077FF))
                ) {
                    Text(text = "See details", color = Color.White)
                }
            }
        }
    }
}

