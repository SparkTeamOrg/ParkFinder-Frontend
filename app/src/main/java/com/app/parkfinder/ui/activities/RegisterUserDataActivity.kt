package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.ui.ValidationResult
import com.app.parkfinder.ui.screens.auth.RegisterUserDataScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validatePhoneNumber
import com.app.parkfinder.utilis.validateUserName

class RegisterUserDataActivity : ComponentActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var verificationCode: String

    private var fullName = mutableStateOf("")
    private var phoneNumber = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        verificationCode = intent.getStringExtra("code") ?: ""

        setContent {
            ParkFinderTheme {
                RegisterUserDataScreen (
                    fullName = fullName.value,
                    onFullNameChange = { fullName.value = it },
                    phoneNumber = phoneNumber.value,
                    onPhoneNumberChange = { phoneNumber.value = it },
                    onBackClick = { finish() },
                    onNextClick = { navigateToVehicleInfoEntry() },
                    isNameValid = { fullName -> isNameValid(fullName) },
                    isPhoneValid = { phoneNumber -> isPhoneValid(phoneNumber) }
                )
            }
        }
    }

    private fun navigateToVehicleInfoEntry(){
        val intent = Intent(this, RegisterVehicleInfoActivity::class.java).apply {
            putExtra("email", email)
            putExtra("password", password)
            putExtra("code", verificationCode)
            putExtra("firstName", fullName.value.split(" ")[0])
            putExtra("lastName", fullName.value.split(" ")[1])
            putExtra("phoneNumber", phoneNumber.value)
            putExtra("profileImage", "")
        }

        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun isNameValid(fullName: String): ValidationResult {
        val validUserName = validateUserName(fullName)
        if (!validUserName) {
            return ValidationResult(success = false, message = "Invalid name")
        }
        return ValidationResult(success = true, message = "")
    }

    private fun isPhoneValid(phoneNumber: String): ValidationResult {
        val validPhoneNumber = validatePhoneNumber(phoneNumber)
        if (!validPhoneNumber) {
            return ValidationResult(success = false, message = "Invalid phone number")
        }
        return ValidationResult(success = true, message = "")
    }
}