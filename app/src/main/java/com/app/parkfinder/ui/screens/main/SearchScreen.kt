package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R

@Composable
fun SearchScreen(
    searchParkingsAroundLocation: (String, Int) -> Unit = { s: String, i: Int -> },
) {
    var searchedLocation by remember{ mutableStateOf("")}
    var radius by remember { mutableFloatStateOf(1f) } // Initial value of radius in km
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF151A24)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.search_title),
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp, top = 30.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.search_parking_img),
            contentDescription = "Parking logo",
            modifier = Modifier.requiredSize(180.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Search location input
        OutlinedTextField(
            value = searchedLocation,
            onValueChange = {searchedLocation = it},
            placeholder = { Text(
                text = stringResource(id = R.string.search_location_hint),
                color = Color.White
            ) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.White) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 2.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(36, 45, 64),
                unfocusedBorderColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.search_choose_radius) + " ${radius.toInt()} km",
            color = Color.White
        )
        Slider(
            value = radius,
            onValueChange = { radius = it },
            valueRange = 1f..30f,
            modifier = Modifier.fillMaxWidth(0.8f),
            steps = 29, // 30 steps including the start and end points
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF3B83F6),
                activeTrackColor = Color(0xFF3B83F6)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Button
        Button(
            onClick = {
                searchParkingsAroundLocation(searchedLocation, radius.toInt())
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3B83F6)),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.common_search),
                color = Color.White
            )
        }
    }
}