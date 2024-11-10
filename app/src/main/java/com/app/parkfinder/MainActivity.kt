package com.app.parkfinder

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.app.parkfinder.ui.activities.NavigationActivity
import com.app.parkfinder.ui.activities.WelcomeActivity
import com.auth0.android.jwt.JWT
import java.util.logging.Logger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)
        val refreshToken = sharedPreferences.getString("refresh_token", null)

        Logger.getLogger("MainActivity").info("Access token: $accessToken")

        if(accessToken != null && !isTokenExpired(accessToken)) {
            // User is logged in and token is not expired
            Logger.getLogger("MainActivity").info("User is logged in")
            val intent = Intent(this, NavigationActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
            startActivity(intent, options.toBundle())
        } else if(refreshToken != null) {
            // Optionally, use the refresh token to get a new access token
            navigateToLogin()   // TODO: Implement refresh token logic
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val jwt = JWT(token)
            jwt.isExpired(10)   // 10 seconds lee-way to account for clock skew
        } catch (e: Exception) {
            true    // If there is an exception, the token is invalid
        }
    }
}