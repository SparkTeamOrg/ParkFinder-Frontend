package com.app.parkfinder.ui.screens.parking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.composables.ParkFinderLogo

data class ParkingSpace(
    val name: String,
    val distance: String,
    val rating: Float
)

@Composable
fun ParkingListScreen(parkingSpaces: List<ParkingLotDto>) {
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
                    ParkingItem(parkingSpace = parkingSpaces[index])
                }
            }
        }
    )
}

@Composable
fun ParkingItem(parkingSpace: ParkingLotDto) {
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
                    text = "⭐ 0.0",
                    fontSize = 14.sp,
                    color = Color.Yellow
                )
                Button(
                    onClick = { /* Handle details click */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0077FF))
                ) {
                    Text(text = "See details", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ParkingScreenPreview(
    parkings: List<ParkingLotDto>
) {
    val parkingSpaces = remember {
        listOf(
            ParkingSpace("Zmaj Jovina 12", "0.2 km away", 5.0f),
            ParkingSpace("Zmaj Jovina 14", "0.25 km away", 4.5f),
            ParkingSpace("Zmaj Jovina 20", "0.3 km away", 4.8f),
            ParkingSpace("Zmaj Jovina 22", "0.31 km away", 4.0f),
            ParkingSpace("Radoja Domanovića", "0.5 km away", 3.8f),
            ParkingSpace("Grada Sirena 16", "0.6 km away", 4.2f)
        )
    }
    ParkingListScreen(parkings)
}
