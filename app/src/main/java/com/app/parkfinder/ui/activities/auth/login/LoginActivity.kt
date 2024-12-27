package com.app.parkfinder.ui.activities.auth.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.app.parkfinder.R
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.AuthStatus
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.activities.NavigationActivity
import com.app.parkfinder.ui.activities.auth.register.RegisterActivity
import com.app.parkfinder.ui.activities.WelcomeActivity
import com.app.parkfinder.ui.screens.auth.login.LoginScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.TranslationHelper
import com.app.parkfinder.utilis.validateEmail
import com.app.parkfinder.utilis.validatePassword

class LoginActivity: ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {

                val email = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }

                LoginScreen(
                    email = email.value,
                    onEmailChange = { email.value = it },
                    password = password.value,
                    onPasswordChange = { password.value = it },
                    onBackClick = { navigateToWelcome() },
                    onForgotPasswordClick = { navigateToForgotPassword() },
                    onRegisterClick = { navigateToRegister() },
                    login = { loginUser(email.value, password.value) },
                    validateEmail = { validateEmail(it) },
                    validatePassword = { validatePassword(it) }
                )
            }
        }

        authViewModel.loginResult.observe(this) { result ->
            if (result.isSuccessful) {
                saveTokens(result.data.accessToken, result.data.refreshToken)
                AuthStatus.resetRefreshTokenExpiredState()
                val intent = Intent(this, NavigationActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                startActivity(intent, options.toBundle())
            } else {
                val translatedMessage = TranslationHelper.getTranslatedMessage(this, result.messages.firstOrNull() ?: "")
                Toast.makeText(this, translatedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginDto = UserLoginDto(email, password)
        authViewModel.login(loginDto)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToForgotPassword()
    {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToWelcome()
    {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun saveTokens(accessToken: String?, refreshToken: String?) {
        AppPreferences.accessToken = accessToken
        AppPreferences.refreshToken = refreshToken
    }
}