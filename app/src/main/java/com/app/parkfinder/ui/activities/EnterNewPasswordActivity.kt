package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.EnterNewPasswordScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class EnterNewPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                EnterNewPasswordScreen(
                    onFinishClick = {navigateToLogin()}
                )
            }
        }
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}