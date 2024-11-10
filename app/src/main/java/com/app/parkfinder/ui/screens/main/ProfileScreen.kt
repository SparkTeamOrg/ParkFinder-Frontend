package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.ui.BottomNavItem
import com.app.parkfinder.ui.screens.common.BottomNavigationBar
import com.app.parkfinder.ui.screens.common.ParkFinderLogo
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun ProfileScreen() {
    Text("Profile")
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ParkFinderTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = { ParkFinderLogo() },
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Profile.route,
                Modifier.padding(innerPadding)
            ) {
                //UI for Home
                composable(BottomNavItem.Home.route) { HomeScreen({}) }
                //UI for Search
                composable(BottomNavItem.Search.route) { SearchScreen() }
                //UI for Profile
                composable(BottomNavItem.Profile.route) { ProfileScreen() }
                //UI for Reserved
                composable(BottomNavItem.Reserved.route){ ReservedScreen() }
            }
        }
    }
}