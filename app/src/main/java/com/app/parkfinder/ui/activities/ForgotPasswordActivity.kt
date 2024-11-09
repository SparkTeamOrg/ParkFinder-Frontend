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
import com.app.parkfinder.ui.screens.auth.ForgotPasswordScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ForgotPasswordActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private val email = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                ForgotPasswordScreen(
                    email = email.value,
                    onEmailChange = { email.value = it },
                    onBackClick = { finish() },
                    onSendClick = { sendVerificationCode() }
                )
            }
        }

        authViewModel.sendingVerificationCodeForPasswordResetResult.observe(this) { result ->
            if (result.isSuccessful) {
                navigateToVerificationCode()
            }
            else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendVerificationCode() {
        authViewModel.sendVerificationCodeForPasswordReset(email.value)
    }

    private fun navigateToVerificationCode() {
        val intent = Intent(this, ResetPasswordVerificationCodeActivity::class.java).apply {
            putExtra("email", email.value)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}