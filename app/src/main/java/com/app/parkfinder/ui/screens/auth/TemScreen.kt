package com.app.parkfinder.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.ui.BottomNavItem
import com.app.parkfinder.ui.screens.SearchContent
import com.app.parkfinder.ui.screens.common.BottomNavigationBar
import com.app.parkfinder.ui.screens.common.ParkFinderLogo

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TempScreen (
    logout: () -> Unit
) {

    val navController = rememberNavController()
    Scaffold(
        topBar = { ParkFinderLogo() },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                Text("Home")
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF151A24))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Temp Screen - You are logged in",
                        color = Color.White
                    )
                    Button(
                        onClick = { logout() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Logout")
                    }
                }
            }
            composable(BottomNavItem.Search.route) { SearchContent() }
            composable(BottomNavItem.Profile.route) { Text("Profile") }
            composable(BottomNavItem.Reserved.route){ Text("Reserved") }
        }
    }

}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TempScreen(
        logout = {}
    )
}