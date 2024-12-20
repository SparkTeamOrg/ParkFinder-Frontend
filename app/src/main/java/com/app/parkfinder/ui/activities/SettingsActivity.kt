package com.app.parkfinder.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.app.parkfinder.MainActivity
import com.app.parkfinder.ui.screens.main.SettingsScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.LocaleHelper

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                SettingsScreen(
                    onLanguageChange = { language ->
                        setPreferredLanguage(language)
                    },
                    onBackClick = { finish() }
                )
            }
        }
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