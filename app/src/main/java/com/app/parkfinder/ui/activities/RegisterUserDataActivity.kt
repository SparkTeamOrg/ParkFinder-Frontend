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

class RegisterUserDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                RegisterUserDataScreen (
                    onBackClick = { finish() },
                    onNextClick = { navigateToVehicleInfoEntry() },
                    isNameValid = { fullName -> isNameValid(fullName) },
                    isPhoneValid = { phoneNumber -> isPhoneValid(phoneNumber) },
                    activityIntent = intent
                )
            }
        }
    }

    private fun navigateToVehicleInfoEntry(){
        val intent = Intent(this, RegisterVehicleInfoActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun isNameValid(fullName: String): ValidationResult {
        val fullNamePattern = "^[A-Z][a-z]+\\s[A-Z][a-z]+$"
        val fullNameRegex = Regex(fullNamePattern)
        return ValidationResult(success = fullNameRegex.matches(fullName), message = "Invalid full name")
    }

    private fun isPhoneValid(phoneNumber: String): ValidationResult {
        val phoneNumberPattern = "^(\\+381|0)\\d{8,9}$"
        val phoneNumberRegex = Regex(phoneNumberPattern)
        return ValidationResult(success = phoneNumberRegex.matches(phoneNumber), message = "Invalid phone number")
    }
}
