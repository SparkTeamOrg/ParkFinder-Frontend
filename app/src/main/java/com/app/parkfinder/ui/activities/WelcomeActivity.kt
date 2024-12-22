package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.app.parkfinder.MainActivity
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.R
import com.app.parkfinder.ui.activities.auth.login.LoginActivity
import com.app.parkfinder.ui.activities.auth.register.RegisterActivity
import com.app.parkfinder.ui.screens.auth.WelcomeScreen
import com.app.parkfinder.utilis.LocaleHelper

class WelcomeActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        setContent {
            ParkFinderTheme {
                WelcomeScreen(
                    onLoginClick = { navigateToLogin() },
                    onRegisterClick = { navigateToRegister() },
                    onLanguageChange = { language -> setPreferredLanguage(language) }
                )
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun setPreferredLanguage(language: String) {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("language", language)
            apply()
        }

        LocaleHelper.setLocale(this, language)

        // Restart the activity to apply the new language
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}