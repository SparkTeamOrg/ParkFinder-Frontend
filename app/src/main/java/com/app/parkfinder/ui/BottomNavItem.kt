package com.app.parkfinder.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.app.parkfinder.R

sealed class BottomNavItem(val route: String, val icon: ImageVector, val labelResId: Int) {
    object Home : BottomNavItem("home", Icons.Default.Home, R.string.menu_home)
    object Search : BottomNavItem("search", Icons.Default.Search, R.string.menu_search)
    object Profile : BottomNavItem("profile", Icons.Default.Person, R.string.menu_profile)
    object Reserved : BottomNavItem("reserved", Icons.Default.AccessTime, R.string.menu_reserved)
}

@Composable
fun bottomNavItemLabel(item: BottomNavItem): String {
    return stringResource(id = item.labelResId)
}