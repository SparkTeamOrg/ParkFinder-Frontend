package com.app.parkfinder.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.app.parkfinder.ui.theme.ParkFinderTheme

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Add your login screen here
        Text(
            text = "Login Screen",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ParkFinderTheme {
        LoginScreen()
    }
}