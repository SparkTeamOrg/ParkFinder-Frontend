package com.app.parkfinder.ui.screens.auth

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.logic.view_models.ReservationViewModel
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
    startFpmNotificationService: ()->Unit = {},
    stopFpmNotificationService: ()->Unit = {},
    logout: () -> Unit,
    user: UserDto,
    currentImageUrl: Uri?,
    openImagePicker: () -> Unit,
    removeImage: () -> Unit,
    searchFreeParkingsAroundLocation: (String,Int) -> Unit = { s: String, i: Int -> },
    confirmReservation: (Int) -> Unit,
    cancelReservation: (Int) -> Unit,
    addReservationHistory: (Int, Int, String) -> Unit,
    navigateToVehicleInfo: () -> Unit = { ->},
    navigateToReservation: (ParkingSpotDto, ParkingLotDto, String) -> Unit,
    navigateToHelpCenter: () -> Unit,
    mapViewModel: MapViewModel = viewModel(),
    reservationViewModel: ReservationViewModel= viewModel(),
    navigateToStatistics: ()->Unit = {}
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
//            val currentRoute = navController.currentBackStackEntry?.destination?.route
//            currentRoute?.let { route ->
//                // Perform specific logic for the clicked navigation item
//                when (route) {
//                    BottomNavItem.Home.route ->{
//                        Log.d("Serviceee","Home route")
//                        mapViewModel.startLocationTrack()
//                    }
//                    else -> {
//                        Log.d("Serviceee","Other routes")
//                        mapViewModel.stopLocationTrack()
//                    }
//                }
//            }
            //UI for Home
            composable(BottomNavItem.Home.route) {
                HomeScreen(user, navigateToReservation,confirmReservation, cancelReservation, mapViewModel)
                MapViewModel.isMapView = true
            }
            //UI for Search
            composable(BottomNavItem.Search.route) {
                MapViewModel.isMapView = false
                SearchScreen(searchFreeParkingsAroundLocation)
            }
            //UI for Profile
            composable(BottomNavItem.Profile.route) {
                MapViewModel.isMapView = false
                ProfileScreen(logout, user, currentImageUrl, openImagePicker, removeImage, navigateToVehicleInfo, startFpmNotificationService,stopFpmNotificationService, navigateToStatistics,navigateToHelpCenter)
            }
            //UI for Reserved
            composable(BottomNavItem.Reserved.route){
                MapViewModel.isMapView = false
                ReservedScreen(reservationViewModel, addReservationHistory)
            }
        }
    }

}




//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    NavigationScreen(
//        logout = {},
//        user = UserDto(),
//        currentImageUrl = null,
//        openImagePicker = {},
//        removeImage = {},
//        navigateToVehicleInfo = {},
//        navigateToReservation = {}
//    )
//}