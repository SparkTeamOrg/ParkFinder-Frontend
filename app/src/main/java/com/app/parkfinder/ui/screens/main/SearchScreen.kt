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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.R
import com.app.parkfinder.ui.BottomNavItem
import com.app.parkfinder.ui.screens.common.BottomNavigationBar
import com.app.parkfinder.ui.screens.common.ParkFinderLogo
import com.app.parkfinder.ui.theme.ParkFinderTheme



@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Find Desired Parking Space",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp, top = 80.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.parking_lot),
            contentDescription = "Parking logo",
            modifier = Modifier.requiredSize(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search location input
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search locations...", color = Color.White) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.White) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(36, 45, 64),
                unfocusedBorderColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Radius slider
        var radius by remember { mutableFloatStateOf(1f) }
        Text(text = "Choose Radius $radius", color = Color.White)
        Slider(
            value = radius,
            onValueChange = { radius = it },
            valueRange = 0.1f..5f,
            modifier = Modifier.fillMaxWidth(0.8f),
            steps = 10,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF3B83F6),
                activeTrackColor = Color(0xFF3B83F6)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Button
        Button(
            onClick = { /* Handle search action */ },//TODO - Add navigation to the home with shown parking lots
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3B83F6)),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(horizontal = 16.dp)
        ) {
            Text("Search", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    ParkFinderTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = { ParkFinderLogo() },
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Search.route,
                Modifier.padding(innerPadding)
            ) {
                //UI for Home
                composable(BottomNavItem.Home.route) { HomeScreen() }
                //UI for Search
                composable(BottomNavItem.Search.route) { SearchScreen() }
                //UI for Profile
                composable(BottomNavItem.Profile.route) { ProfileScreen({}) }
                //UI for Reserved
                composable(BottomNavItem.Reserved.route){ ReservedScreen() }
            }
        }
    }
}