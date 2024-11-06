package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Observer
import com.app.parkfinder.MainActivity
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.screens.LoginScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class LoginActivity: ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {

                var email = remember { mutableStateOf("") }
                var password = remember { mutableStateOf("") }

                LoginScreen(
                    email = email.value,
                    onEmailChange = { email.value = it },
                    password = password.value,
                    onPasswordChange = { password.value = it },
                    onBackClick = { finish() },
                    onForgotPasswordClick = { navigateToForgotPassword() },
                    onRegisterClick = { navigateToRegister() },
                    login = { loginUser(email.value, password.value) },
                    validateEmail = { validateEmail(it) }
                )
            }
        }

        authViewModel.loginResult.observe(this, Observer { result ->
            if (result.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("token", result.data)
                }
                val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                startActivity(intent, options.toBundle())
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = "^[\\w-.]+@[\\w-]+\\.[a-z]{2,3}$".toRegex(RegexOption.IGNORE_CASE)
        return emailRegex.matches(email)
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
}