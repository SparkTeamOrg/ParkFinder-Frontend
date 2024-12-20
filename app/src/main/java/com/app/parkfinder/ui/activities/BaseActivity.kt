package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.app.parkfinder.R
import com.app.parkfinder.logic.AuthStatus

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthStatus.isRefreshTokenExpired.observe(this) { isExpired ->
            if (isExpired) {
                Toast.makeText(this,"Your session has expired." , Toast.LENGTH_LONG).show()
                AuthStatus.resetRefreshTokenExpiredState()
                navigateToWelcomeScreen()
            }
        }
    }

    private fun navigateToWelcomeScreen() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}