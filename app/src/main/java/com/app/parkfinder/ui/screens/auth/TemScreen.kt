package com.app.parkfinder.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TempScreen (
    logout: () -> Unit
) {
    Column (
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TempScreen(
        logout = {}
    )
}