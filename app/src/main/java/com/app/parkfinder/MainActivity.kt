package com.app.parkfinder

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.dtos.TokenDto
import com.app.parkfinder.logic.services.TokenService
import com.app.parkfinder.ui.activities.NavigationActivity
import com.app.parkfinder.ui.activities.WelcomeActivity
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)
        val accessToken = AppPreferences.accessToken
        val refreshToken = AppPreferences.refreshToken
        Logger.getLogger("MainActivity").info("Access token: $accessToken")
        Logger.getLogger("MainActivity").info("Refresh token: $refreshToken")

        if(accessToken != null && !isTokenExpired(accessToken)) {
            // User is logged in and token is not expired
            Logger.getLogger("MainActivity").info("User is logged in")
            navigateToNavigationPage()
        } else if(refreshToken != null) {
            // Optionally, use the refresh token to get a new access token
            val refreshSuccess = refreshToken(accessToken,refreshToken)
            if(refreshSuccess) navigateToNavigationPage() else logOutUser()

        } else {
            logOutUser()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToNavigationPage(){
        val intent = Intent(this, NavigationActivity::class.java)
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

    private fun refreshToken(accessToken: String?,refreshToken: String) : Boolean {
        val tokenService = RetrofitConfig.createService(TokenService::class.java)
        val refreshResponse = runBlocking {
            tokenService.refresh(TokenDto(accessToken, refreshToken))
        }
        if (refreshResponse.isSuccessful) {
            val body = refreshResponse.body()
            if (body != null && body.isSuccessful) {
                val newTokens = body.data
                AppPreferences.accessToken = newTokens.accessToken
                AppPreferences.refreshToken = newTokens.refreshToken
                return true
            }
        }
        return false
    }

    private fun logOutUser(){
        navigateToLogin()
        AppPreferences.removeTokens()
    }
}