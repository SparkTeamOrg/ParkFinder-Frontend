package com.app.parkfinder.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.ui.screens.RegisterScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class RegisterActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                RegisterScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}