package com.app.parkfinder.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.compose.setContent
import com.app.parkfinder.MainActivity
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.main.SettingsScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.LocaleHelper
import com.bumptech.glide.Glide

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                SettingsScreen(
                    onLanguageChange = { language ->
                        setPreferredLanguage(language)
                    },
                    onBackClick = { finish() },
                    getPreferredLanguage = { getPreferredLanguage() }
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

        // Display the GIF animation
        val imageView = ImageView(this)

        setContentView(imageView)
        Glide.with(this).load(R.drawable.loader).into(imageView)

        // Start MainActivity after the animation
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }, 2000) // Adjust the delay to match the duration of your GIF
    }

    private fun getPreferredLanguage(): String {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", "en") ?: "en"
    }
}