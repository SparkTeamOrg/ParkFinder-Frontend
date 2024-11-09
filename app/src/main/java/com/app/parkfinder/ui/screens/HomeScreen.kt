package com.app.parkfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.ui.screens.common.BottomNavigationBar
import com.app.parkfinder.ui.screens.common.NavigationHost
import com.app.parkfinder.ui.screens.common.ParkFinderLogo

@Composable
fun HomeScreen()
{
    val navController = rememberNavController()
    Scaffold(
        topBar = { ParkFinderLogo() },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationHost(navController)
        }
    }
}