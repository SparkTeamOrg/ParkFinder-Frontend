package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.VerificationCodeScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class VerificationCodeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                VerificationCodeScreen(
                    onResendClick = {},
                    onBackClick = {finish()},
                    onNextClick = {navigateToEnterNewPassword()}
                )
            }
        }
    }
    private fun navigateToEnterNewPassword() {
        val intent = Intent(this, RegisterUserDataActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}