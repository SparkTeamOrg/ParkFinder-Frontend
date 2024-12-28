package com.app.parkfinder.ui.screens.main

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ReservationCommentDto
import com.app.parkfinder.logic.models.dtos.VehicleDto
import com.app.parkfinder.utilis.formatDate
import kotlin.math.round

@SuppressLint("DefaultLocale")
@Composable
fun ReservationScreen(
    lot: ParkingLotDto,
    spotNumber: String,
    comments: List<ReservationCommentDto>,
    rating: Double,
    startNavigation: () -> Unit,
    vehicles: List<VehicleDto>,
    selectedVehicle: Int,
    onSelectedVehicleChange: (Int)->Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(280.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.parking_space),  // Replace with your image resource
            contentDescription = "Parking Spot",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp, start = 5.dp)
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF151A24), shape = CircleShape)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = White
                )
            }
            Text(
                text = stringResource(id = R.string.reservation_screen_space_details),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 250.dp)
            .background(
                Color(0xFF151A24),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
        ) {
        Box(
            modifier = Modifier
                .background(
                    Color(0xFF151A24),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        Modifier.fillMaxWidth()
                            .weight(0.8f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = (
                                if (lot.road != null)
                                    lot.road.toString()
                                else stringResource(id = R.string.reservation_screen_unknown_road)
                            ),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = White,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            lot.city?.let {
                                Text(
                                    text = it,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White.copy(alpha = 0.3f),
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                            }
                            Text(
                                text = "${round(lot.distance * 100) / 100} km " + stringResource(id = R.string.common_away).lowercase(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00AEEF),
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

                Column(
                    modifier = Modifier.fillMaxSize()
                ){
                    Spacer(modifier = Modifier.height(5.dp))

                    RatingStars(rating)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(id = R.string.reservation_screen_spot_number),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Divider(
                        color = White.copy(alpha = 0.3f),
                        thickness = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = spotNumber,
                        fontSize = 18.sp,
                        color = Color(0xFF00AEEF)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(id = R.string.reservation_screen_spot_status),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                    )
                    Divider(
                        color = White.copy(alpha = 0.3f),
                        thickness = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.parking_spot_status_free),
                        fontSize = 18.sp,
                        color = Color(0xFF00AEEF)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(id = R.string.reservation_screen_choose_vehicle),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                    )
                    Divider(
                        color = White.copy(alpha = 0.3f),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 7.dp)
                    )
                    OutlinedDropdownMenu(
                        label = "",
                        selectedText = stringResource(id = R.string.reservation_screen_select_vehicle),
                        options = vehicles,
                        icon = Icons.Default.DirectionsCar,
                        onOptionSelected = { option -> onSelectedVehicleChange(option) }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = { startNavigation() },
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
                                text = stringResource(id = R.string.reservation_screen_reserve_and_navigate),
                                color = if (selectedVehicle != 0) White else White.copy(alpha = 0.3f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.reservation_screen_reviews),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = White,
                        )
                        Text(
                            text = " (${comments.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = White.copy(alpha = 0.5f),
                        )
                    }
                    Divider(
                        color = White.copy(alpha = 0.3f),
                        thickness = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (comments.isNotEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp)
                        ) {
                            items(comments) { comment ->
                                CommentCard(comment)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Comment,
                                contentDescription = "Comment",
                                Modifier.size(60.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = stringResource(id = R.string.reservation_screen_no_reviews),
                                fontSize = 18.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CommentCard(comment: ReservationCommentDto) {
    Card(
        modifier = Modifier
            .size(200.dp, 260.dp),
        elevation = 4.dp,
        backgroundColor = Color(0xFF333333),
        border = BorderStroke(1.dp, White.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(modifier = Modifier.padding(8.dp)){
                Box(modifier = Modifier.size(50.dp)){
                    var uri: Uri? = null
                    if(comment.userProfileImage != null) {
                        uri = Uri.parse(comment.userProfileImage)
                    }
                    ProfileImage(uri, false)
                }
                Column(
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    Text(
                        text = comment.userFirstName + " " + comment.userLastName,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (comment.rating > index) Color.Yellow else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.height(165.dp)) {
                Text(
                    text = comment.comment,
                    color = White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    textAlign = TextAlign.Left
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, end = 5.dp),
                text = formatDate(comment.endTime),
                textAlign = TextAlign.Right,
                fontSize = 12.sp,
                color = White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun RatingStars(rating: Double){
    Row {
        if(rating != 0.0) {
            repeat(5) { index ->
                val starColor = when {
                    rating >= index + 1 -> Color.Yellow
                    rating >= index + 0.5 -> Color.Yellow.copy(alpha = 0.5f)
                    else -> Color.Gray
                }

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = starColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }else{
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Color.Gray,
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    text = stringResource(id = R.string.reservation_screen_not_rated_yet),
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 5.dp)
                )
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
                unfocusedContainerColor = Color(0xFF151A24),
                unfocusedBorderColor = White.copy(alpha = 0.3f),
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