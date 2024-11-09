package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.screens.ResetPasswordVerificationCodeScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import java.util.logging.Logger

class ResetPasswordVerificationCodeActivity : ComponentActivity() {

    private lateinit var email: String

    private val authViewModel: AuthViewModel by viewModels()
    private val otpValues = mutableStateOf(List(4) { "" })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra("email") ?: ""

        setContent {
            ParkFinderTheme {
                ResetPasswordVerificationCodeScreen(
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

        authViewModel.sendingVerificationCodeForPasswordResetResult.observe(this) { result ->
            if (result.isSuccessful) {
                Toast.makeText(this, "Verification code sent", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }

        authViewModel.verifyingCodeResult.observe(this) { result ->
            if (result.isSuccessful) {
                navigateToNextScreen()
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this, EnterNewPasswordActivity::class.java).apply {
            putExtra("email", email)
            putExtra("code", otpValues.value.joinToString(""))
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun verifyVerificationCode() {
        val code = otpValues.value.joinToString("")
        Logger.getLogger("ResetPasswordVerificationCodeActivity").info("Verifying code: $code")
        authViewModel.verifyVerificationCode(email, code)
    }

    private fun sendVerificationCode(email: String) {
        authViewModel.sendVerificationCodeForPasswordReset(email)
    }
}