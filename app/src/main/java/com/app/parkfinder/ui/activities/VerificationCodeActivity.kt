package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.R
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.screens.VerificationCodeScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class VerificationCodeActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = intent.getStringExtra("email")

        setContent {
            ParkFinderTheme {
                VerificationCodeScreen(
                    email = email ?: "",
                    onResendClick = { sendVerificationCode(email ?: "") },
                    onBackClick = {finish()},
                    onNextClick = {navigateToEnterNewPassword()},
                    activityIntent = intent
                )
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

    private fun navigateToEnterNewPassword() {
        val intent = Intent(this, RegisterUserDataActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun sendVerificationCode(email: String) {
        authViewModel.sendVerificationCodeForRegistration(email)
    }
}