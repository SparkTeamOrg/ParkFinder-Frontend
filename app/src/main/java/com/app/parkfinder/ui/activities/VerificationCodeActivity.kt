package com.app.parkfinder.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.screens.VerificationCodeScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class VerificationCodeActivity : ComponentActivity() {

    private lateinit var email: String

    private val authViewModel: AuthViewModel by viewModels()
    private val otpValues = mutableStateOf(List(4) { "" })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra("email") ?: ""

        setContent {
            ParkFinderTheme {
                VerificationCodeScreen(
                    email = email ?: "",
                    otpValues = otpValues.value,
                    onOtpValueChange = { newOtpValues ->
                        otpValues.value = newOtpValues
                    },
                    onBackClick = {finish()},
                    onNextClick = { verifyVerificationCode() },
                    onResendClick = { sendVerificationCode(email ?: "") }
                )
            }
        }

        authViewModel.verifyingCodeResult.observe(this) { result ->
            if (result.isSuccessful) {
                navigateToNextScreen()
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }

        authViewModel.sendingVerificationCodeForRegistrationResult.observe(this) { result ->
            if (result.isSuccessful) {
                Toast.makeText(this, "Verification code sent", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToNextScreen() {
        // navigate to RegisterUserDataActivity with email, password and verification code as extras
    }

    private fun verifyVerificationCode() {
        val code = otpValues.value.joinToString("")
        authViewModel.verifyVerificationCode(email, code)
    }

    private fun sendVerificationCode(email: String) {
        authViewModel.sendVerificationCodeForRegistration(email)
    }
}