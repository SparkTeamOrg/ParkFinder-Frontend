package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.view_models.ReservationViewModel
import com.app.parkfinder.utilis.formatDate


@Composable
fun ReservedScreen(
    reservationViewModel: ReservationViewModel,
    addReservationHistory: (Int, Int, String) -> Unit
) {

    LaunchedEffect(Unit) {
        reservationViewModel.getConfirmedReservation()
    }

    val confirmedReservations by reservationViewModel.getConfirmedReservationResult.observeAsState(
        BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList())
    )
    val pagerState = rememberPagerState(pageCount = { confirmedReservations.data.size })

    if(confirmedReservations.data.isNotEmpty()) {
        Box(
            modifier = Modifier
                .height(250.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.parking_space),
                contentDescription = "Parking Spot",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
            )
            Text(
                text = "My Reservations",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 220.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFF151A24),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            ) {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(16.dp)
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxSize()
                ) { page ->
                    key(page) {
                        val vehicleId = confirmedReservations.data[page].vehicleId
                        val vehicleBrand =
                            confirmedReservations.data[page].vehicleVehicleModelVehicleBrandName
                        val vehicleModel = confirmedReservations.data[page].vehicleVehicleModelName
                        val licencePlate = confirmedReservations.data[page].vehicleLicencePlate
                        val road = confirmedReservations.data[page].parkingSpotParkingLotRoad
                        val city = confirmedReservations.data[page].parkingSpotParkingLotCity
                        val startTime = confirmedReservations.data[page].confirmationTime
                        val zone =
                            confirmedReservations.data[page].parkingSpotParkingLotParkingZoneName

                        var comment by remember { mutableStateOf("") }
                        var rating by remember { mutableIntStateOf(0) }

                        Column(
                            modifier = Modifier.fillMaxSize()
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .verticalScroll(ScrollState(0))
                        ) {
                            // Location Title and Bookmark Icon
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    Modifier
                                        .weight(0.8f)
                                        .height(50.dp)
                                ) {
                                    Text(
                                        text = (road),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = city,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = White.copy(alpha = 0.3f),
                                            modifier = Modifier.padding(end = 10.dp)
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
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Zone",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Divider(
                                color = White.copy(alpha = 0.3f),
                                thickness = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = zone,
                                fontSize = 18.sp,
                                color = Color(0xFF00AEEF)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Start time",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Divider(
                                color = White.copy(alpha = 0.3f),
                                thickness = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = formatDate(startTime),
                                fontSize = 18.sp,
                                color = Color(0xFF00AEEF)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Vehicle",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Divider(
                                color = White.copy(alpha = 0.3f),
                                thickness = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "$vehicleBrand $vehicleModel $licencePlate",
                                fontSize = 18.sp,
                                color = Color(0xFF00AEEF)
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Finish your reservation",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                    .padding(bottom = 10.dp)
                            )
                            Divider(
                                color = White.copy(alpha = 0.3f),
                                thickness = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            // Rate parking spot
                            Text(
                                text = "Rate parking spot",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Star Rating
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(5) { index ->
                                    val starColor = if (index < rating) Color.Yellow else Color.Gray
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star",
                                        tint = starColor,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                rating = index + 1
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Comment section
                            Text(
                                text = "Leave comment (optional)",
                                fontSize = 16.sp,
                                color = White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Comment TextField
                            OutlinedTextField(
                                value = comment,
                                onValueChange = { comment = it },
                                placeholder = {
                                    Text(
                                        text = "Write comment here...",
                                        color = Color.Gray
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    backgroundColor = Color(0xFF2A2A2A),
                                    cursorColor = Color.White,
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.Gray,
                                    textColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Please leave a comment for a rating below 3 stars.",
                                fontSize = 14.sp,
                                color = White.copy(alpha = 0.3f),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(30.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Button(
                                    onClick = {
                                        addReservationHistory(vehicleId, rating, comment)
                                        comment = ""
                                        rating = 0
                                    },
                                    modifier = Modifier
                                        .width(220.dp)
                                        .height(48.dp)
                                        .align(Alignment.Center),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFF0FCFFF),
                                        disabledBackgroundColor = Color(0xFF0FCFFF).copy(alpha = 0.3f)
                                    ),
                                    enabled = (rating >= 3 || (rating<3 && comment.isNotEmpty())),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Rate and Leave",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF151A24))
                .padding(top = 80.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .background(White.copy(alpha = 0.1f), shape = CircleShape)
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = "No active reservations",
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.Center),
                    tint = Color(0xFF151A24)
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No active reservations",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = White.copy(alpha = 0.3f),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ReservedScreenPreview() {
//    ParkFinderTheme {
//        val navController = rememberNavController()
//        Scaffold(
//            topBar = { ParkFinderLogo() },
//            bottomBar = { BottomNavigationBar(navController = navController) }
//        ) { innerPadding ->
//            NavHost(
//                navController = navController,
//                startDestination = BottomNavItem.Reserved.route,
//                Modifier.padding(innerPadding)
//            ) {
//                //UI for Home
//                composable(BottomNavItem.Home.route) { HomeScreen(UserDto(), {}) }
//                //UI for Search
//                composable(BottomNavItem.Search.route) { SearchScreen() }
//                //UI for Profile
//                composable(BottomNavItem.Profile.route) { ProfileScreen({}, UserDto(), null, {}, {}, {}) }
//                //UI for Reserved
//                composable(BottomNavItem.Reserved.route){ ReservedScreen() }
//            }
//        }
//    }
//}