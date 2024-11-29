package com.app.parkfinder.ui.activities.auth.register

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.auth.register.VerificationCodeScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class VerificationCodeActivity : BaseActivity() {

    private lateinit var email: String
    private lateinit var password: String

    private val authViewModel: AuthViewModel by viewModels()
    private val otpValues = mutableStateOf(List(4) { "" })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""

        setContent {
            ParkFinderTheme {
                VerificationCodeScreen(
                    email = email,
                    otpValues = otpValues.value,
                    onOtpValueChange = { newOtpValues ->
                        otpValues.value = newOtpValues
                    },
                    onBackClick = {finish()},
                    onNextClick = { verifyVerificationCode() },
                    onResendClick = { sendVerificationCode(email) }
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
        val intent = Intent(this, RegisterUserDataActivity::class.java).apply {
            putExtra("email", email)
            putExtra("password", password)
            putExtra("code", otpValues.value.joinToString(""))
        }

        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun verifyVerificationCode() {
        val code = otpValues.value.joinToString("")
        authViewModel.verifyVerificationCode(email, code)
    }

    private fun sendVerificationCode(email: String) {
        authViewModel.sendVerificationCodeForRegistration(email)
    }
}