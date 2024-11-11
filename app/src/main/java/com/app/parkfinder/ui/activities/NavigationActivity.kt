package com.app.parkfinder.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.ui.screens.auth.NavigationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.auth0.android.jwt.JWT
import org.osmdroid.config.Configuration

class NavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
            ParkFinderTheme {
                NavigationScreen(
                    logout = { logout() },
                    user = decodeJwt()
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
    private fun decodeJwt() : UserDto {
        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token","")
        val dto = UserDto()
        try {
            val jwt = JWT(token!!)
            // Get specific claims by name
            dto.Id = jwt.getClaim("UserId").asInt()!!
            dto.Fullname = jwt.getClaim("Fullname").asString()!!

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dto
    }

}