package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.R
import com.app.parkfinder.ui.ValidationResult
import com.app.parkfinder.ui.screens.RegisterUserDataScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class RegisterVehicleInfoActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                RegisterUserDataScreen (
                    onBackClick = { finish() },
                    onNextClick = { navigateToLogin() },
                    isRegisterNumberValid = { number -> isRegisterNumberValid(number) },
                    activityIntent = intent
                )
            }
        }
    }
    private fun navigateToLogin() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
    private fun isRegisterNumberValid(regNum: String): ValidationResult {
        val regNumPattern = "^[A-Z]{2}\\d{3}[A-Z]{2}\$"
        val regNumRegex = Regex(regNumPattern)
        //check if register number already exists (TO DO)
        return ValidationResult(success = regNumRegex.matches(regNum), message = "Invalid register number")
    }
}