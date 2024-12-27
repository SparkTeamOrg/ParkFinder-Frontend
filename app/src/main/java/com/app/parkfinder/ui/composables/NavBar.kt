package com.app.parkfinder.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.parkfinder.ui.BottomNavItem
import com.app.parkfinder.ui.bottomNavItemLabel

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Profile,
        BottomNavItem.Reserved
    )

    BottomNavigation(
        backgroundColor = Color(0xFF151A24),  // Dark background
        contentColor = Color.White
    ) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route
            BottomNavigationItem(
                icon = {
                    if (isSelected) {
                        // Selected item icon with a circular blue background
                        Box(
                            modifier = Modifier
                                .background(Color.Cyan, shape = CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = bottomNavItemLabel(item),
                                tint = Color.Gray
                            )
                        }
                    } else {
                        // Unselected item icon
                        Icon(
                            imageVector = item.icon,
                            contentDescription = bottomNavItemLabel(item),
                            tint = Color.White
                        )
                    }
                },
                label = {
                    Text(
                        text = bottomNavItemLabel(item),
                        color = if (isSelected) Color.Cyan else Color.White,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Pop up to the start destination to prevent back stack accumulation
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                selectedContentColor = Color.Cyan,
                unselectedContentColor = Color.White
            )
        }
    }
}
