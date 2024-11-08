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
import com.app.parkfinder.ui.ValidationResult
import com.app.parkfinder.ui.screens.RegisterScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validateEmail
import com.app.parkfinder.utilis.validatePassword

class RegisterActivity: ComponentActivity() {

    private val email = mutableStateOf("")
    private val password = mutableStateOf("")
    private val confirmedPassword = mutableStateOf("")
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                RegisterScreen(
                    email = email.value,
                    onEmailChange = { email.value = it },
                    password = password.value,
                    onPasswordChange = { password.value = it },
                    confirmedPassword = confirmedPassword.value,
                    onConfirmedPasswordChange = { confirmedPassword.value = it },
                    onBackClick = { finish() },
                    onLoginClick = { navigateToLogin() },
                    onNextClick = { sendVerificationCode(email.value) },
                    validateEmail = { validateEmail(email.value) },
                    validatePasswords = { isValidPasswords(password.value, confirmedPassword.value) }
                )
            }
        }

        authViewModel.sendingVerificationCodeForRegistrationResult.observe(this) { result ->
            if (result.isSuccessful) {
                navigateToVerificationPage(email.value)
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun sendVerificationCode(email: String) {
        authViewModel.sendVerificationCodeForRegistration(email)
    }

    private fun navigateToVerificationPage(email: String) {
        val intent = Intent(this, VerificationCodeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("password", password.value)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun isValidPasswords(password: String, confirmedPassword: String): ValidationResult {
        if(password.isEmpty()){
            return ValidationResult(success = false, message = "Password cannot be empty")
        }
        else if(password != confirmedPassword){
            return ValidationResult(success = false, message = "Passwords do not match")
        }
        else if(!validatePassword(password)){
            return ValidationResult(success = false, message = "Incorrect password format")
        }

        return ValidationResult(success = true, message = "")
    }
}