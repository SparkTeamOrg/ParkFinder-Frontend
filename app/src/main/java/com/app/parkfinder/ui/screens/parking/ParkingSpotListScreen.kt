package com.app.parkfinder.ui.screens.parking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.ui.composables.ParkFinderLogo

@Composable
fun ParkingSpotListScreen(
    parkingSpaces: List<ParkingSpotDto>,
    parkingLotName: String = "",
    navigateToReservation: (ParkingSpotDto, String) -> Unit
) {
    Scaffold(
        topBar = {
            ParkFinderLogo()
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).background(Color(0xFF1E1E1E))) {
                Text(
                    text = parkingLotName,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color(0xFF1E1E1E))
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(parkingSpaces.size) { index ->
                        ParkingSpotItem(parkingSpace = parkingSpaces[index], index = index, navigateToReservation = navigateToReservation)
                    }
                }
            }
        }
    )
}


@Composable
fun ParkingSpotItem(parkingSpace: ParkingSpotDto, index: Int,navigateToReservation: (ParkingSpotDto, String) -> Unit,) {

    // Map the status to a color
    val statusColor = when (ParkingSpotStatusEnum.fromValue(parkingSpace.parkingSpotStatus)) {
        ParkingSpotStatusEnum.FREE -> Color.Green
        ParkingSpotStatusEnum.RESERVED -> Color.Yellow
        ParkingSpotStatusEnum.OCCUPIED -> Color.Red
        ParkingSpotStatusEnum.TEMPORARILY_UNAVAILABLE -> Color.Gray
        else -> Color.Red
    }

    val textShow = when (ParkingSpotStatusEnum.fromValue(parkingSpace.parkingSpotStatus)) {
        ParkingSpotStatusEnum.FREE -> ParkingSpotStatusEnum.FREE.name
        ParkingSpotStatusEnum.RESERVED -> ParkingSpotStatusEnum.RESERVED.name
        ParkingSpotStatusEnum.OCCUPIED -> ParkingSpotStatusEnum.OCCUPIED.name
        ParkingSpotStatusEnum.TEMPORARILY_UNAVAILABLE -> ParkingSpotStatusEnum.TEMPORARILY_UNAVAILABLE.name
        else -> ParkingSpotStatusEnum.OCCUPIED.name
    }

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
            // Status Circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(statusColor, shape = RoundedCornerShape(50))
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

            // Details Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Spot number: P${index+1}",
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = textShow,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Reserve Button
            Button(
                onClick = { navigateToReservation(parkingSpace,"P${index+1}")},
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = Color.Red,
                    backgroundColor = if (parkingSpace.parkingSpotStatus == ParkingSpotStatusEnum.FREE.value) Color(0xFF0077FF) else Color.Red
                ),
                enabled = parkingSpace.parkingSpotStatus == ParkingSpotStatusEnum.FREE.value,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Reserve",
                    color = Color.White
                )
            }
        }
    }
}

