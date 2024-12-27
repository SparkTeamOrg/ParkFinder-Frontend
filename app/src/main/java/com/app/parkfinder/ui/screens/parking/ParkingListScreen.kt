package com.app.parkfinder.ui.screens.parking

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.ui.composables.ParkFinderLogo
import kotlin.math.round
import com.app.parkfinder.R

@SuppressLint("DefaultLocale")
@Composable
fun ParkingListScreen(
    parkingSpaces: List<ParkingLotDto>,
    navigateToParkingSpots: (lot:ParkingLotDto,name:String)->Unit = { _: ParkingLotDto, _: String -> },
    isLoading: Boolean
) {
    Scaffold(
        topBar = {
            ParkFinderLogo()
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF151A24))
                    .padding(paddingValues)
            ) {
                items(parkingSpaces.size) { index ->
                    val roundedValue = round(parkingSpaces[index].distance * 100) / 100
                    parkingSpaces[index].distance = roundedValue
                    ParkingItem(parkingSpace = parkingSpaces[index], navigateToSpots = navigateToParkingSpots)
                }

                item{
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 300.dp),
                            contentAlignment = Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(
                                    100.dp
                                )
                            )
                        }
                    }
                }

                item {
                    if (parkingSpaces.isEmpty() && !isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 250.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Center)
                                    .wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.parking_list_no_parking_lots_found),
                                    fontSize = 24.sp,
                                    color = Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = "No parking lot found",
                                    modifier = Modifier.size(300.dp),
                                    tint = Gray
                                )
                            }
                        }
                    }
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
                    modifier = Modifier.align(Center)
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
                    text = "${parkingSpace.distance} km " + stringResource(id = R.string.common_away).lowercase(),
                    fontSize = 14.sp,
                    color = Gray
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
                    Text(
                        text = stringResource(id = R.string.parking_list_see_details),
                        color = Color.White
                    )
                }
            }
        }
    }
}