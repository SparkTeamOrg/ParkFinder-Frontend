package com.app.parkfinder

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.models.dtos.TokenDto
import com.app.parkfinder.logic.view_models.TokenViewModel
import com.app.parkfinder.ui.activities.NavigationActivity
import com.app.parkfinder.ui.activities.WelcomeActivity
import com.app.parkfinder.utilis.LocaleHelper
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.runBlocking
import com.app.parkfinder.utilis.isTokenExpired

class MainActivity : ComponentActivity() {
    private val tokenViewModel: TokenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = getPreferredLanguage()
        LocaleHelper.setLocale(this, language)
        AppPreferences.setup(applicationContext)
        val accessToken = AppPreferences.accessToken
        val refreshToken = AppPreferences.refreshToken

        if(accessToken != null && !isTokenExpired(accessToken)) {
            // User is logged in and token is not expired
            navigateToNavigationPage()
        } else if(refreshToken != null) {
            // Optionally, use the refresh token to get a new access token
            tryTokenRefresh(accessToken, refreshToken)
        } else {
            logOutUser()
        }

        tokenViewModel.refreshTokensResult.observe(this) { result ->
            if (result.isSuccessful) {
                val newTokens = result.data
                AppPreferences.accessToken = newTokens.accessToken
                AppPreferences.refreshToken = newTokens.refreshToken
                navigateToNavigationPage()
            }
            else {
                logOutUser()
            }
        }
    }

    private fun tryTokenRefresh(accessToken: String?,refreshToken: String){
        tokenViewModel.refreshTokens(TokenDto(accessToken, refreshToken))
    }

    private fun logOutUser(){
        navigateToLogin()
        AppPreferences.removeTokens()
    }

    private fun getPreferredLanguage(): String {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", "en") ?: "en"
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
}