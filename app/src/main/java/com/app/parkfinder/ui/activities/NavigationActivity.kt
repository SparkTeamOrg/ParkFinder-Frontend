package com.app.parkfinder.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.ui.screens.auth.NavigationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class NavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                NavigationScreen(
                    logout = { logout() }
                )
            }
        }
    }

    private fun logout() {
        clearTokens()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearTokens() {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("access_token")
            remove("refresh_token")
            apply()
        }
    }

}