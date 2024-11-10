package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.filled.WifiCalling
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.ui.BottomNavItem
import com.app.parkfinder.ui.screens.common.BottomNavigationBar
import com.app.parkfinder.ui.screens.common.ParkFinderLogo
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun ProfileScreen(
    logout : ()->Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier.size(180.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.default_profile_picture),
                contentDescription = "Background Image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .padding(end = 10.dp, bottom = 12.dp)
                    .align(Alignment.BottomEnd),
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(width = 3.dp, color = Color.White, shape = CircleShape)
                        .background(Color(0xFF0FCFFF))

                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Upload Image",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = "User: Full name", //TODO - add the actual name
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Email
        Text(
            text = "email", //TODO - add the actual email
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Menu Items
        MenuItem(icon = Icons.Default.Wallet, title = "Balance")
        MenuItem(icon = Icons.Default.DirectionsCar, title = "Vehicle info")
        MenuItem(icon = Icons.Default.StackedBarChart, title = "Statistics")
        MenuItem(icon = Icons.Default.Favorite, title = "Favourites")
        MenuItem(icon = Icons.Outlined.Notifications, title = "Notifications", notificationCount = 5)
        MenuItem(icon = Icons.AutoMirrored.Filled.HelpOutline, title = "Help Center")

        Spacer(modifier = Modifier.weight(1f))

        // Log Out
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { logout() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,  // Replace with your logout icon resource
                contentDescription = "Log Out",
                tint = Color.Red,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Log Out",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, title: String, notificationCount: Int? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        if (notificationCount != null && notificationCount > 0) {
            Box(
                modifier = Modifier
                    .background(Color.Red, shape = CircleShape)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$notificationCount",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
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