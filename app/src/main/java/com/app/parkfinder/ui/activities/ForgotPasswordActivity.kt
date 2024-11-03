package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.ForgotPasswordScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ForgotPasswordActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                ForgotPasswordScreen(
                    onBackClick = { finish() },
                    onSendClick = {navigateToVerificationCode()}
                )
            }
        }
    }
    private fun navigateToVerificationCode() {
        val intent = Intent(this, VerificationCodeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}