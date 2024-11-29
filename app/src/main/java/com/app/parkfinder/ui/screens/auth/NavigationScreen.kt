package com.app.parkfinder.ui.screens.auth

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.ui.BottomNavItem
import com.app.parkfinder.ui.composables.BottomNavigationBar
import com.app.parkfinder.ui.composables.ParkFinderLogo
import com.app.parkfinder.ui.screens.main.HomeScreen
import com.app.parkfinder.ui.screens.main.ProfileScreen
import com.app.parkfinder.ui.screens.main.ReservedScreen
import com.app.parkfinder.ui.screens.main.SearchScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NavigationScreen (
    logout: () -> Unit,
    user: UserDto,
    currentImageUrl: Uri?,
    openImagePicker: () -> Unit,
    removeImage: () -> Unit
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
            //UI for Home
            composable(BottomNavItem.Home.route) { HomeScreen(user) }
            //UI for Search
            composable(BottomNavItem.Search.route) { SearchScreen() }
            //UI for Profile
            composable(BottomNavItem.Profile.route) { ProfileScreen(logout, user, currentImageUrl, openImagePicker, removeImage) }
            //UI for Reserved
            composable(BottomNavItem.Reserved.route){ ReservedScreen() }
        }
    }

}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NavigationScreen(
        logout = {},
        user = UserDto(),
        currentImageUrl = null,
        openImagePicker = {},
        removeImage = {}
    )
}