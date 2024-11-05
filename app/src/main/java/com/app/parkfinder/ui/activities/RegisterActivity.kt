package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.R
import com.app.parkfinder.ui.ValidationResult
import com.app.parkfinder.ui.screens.RegisterScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class RegisterActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                RegisterScreen(
                    onBackClick = { finish() },
                    onLoginClick = { navigateToLogin() },
                    onNextClick = { email -> navigateToVerificationPage(email) },
                    isValidEmail = { email -> isValidEmail(email) },
                    isValidPassword = { password, repeatedPassword -> isValidPasswords(password, repeatedPassword)}
                )
            }
        }
    }

    private fun navigateToVerificationPage(email: String) {
        val intent = Intent(this, VerificationCodeActivity::class.java).apply {
            putExtra("email", email)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToLogin() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun isValidEmail(email: String): ValidationResult{
        // TO DO: Check if email already exists
        val validEmailFormat = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        return if (email.isEmpty()){
            ValidationResult(success = false, message = "Email cannot be empty")
        }
        else if(validEmailFormat){
            ValidationResult(success = true, message = "")
        }
        else{
            ValidationResult(success = false, message = "Invalid email format")
        }
    }

    private fun isValidPasswords(password: String, confirmedPassword: String): ValidationResult {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$"
        val passwordRegex = Regex(passwordPattern)

        if(password.isEmpty()){
            return ValidationResult(success = false, message = "Password cannot be empty")
        }
        else if(password != confirmedPassword){
            return ValidationResult(success = false, message = "Passwords do not match")
        }
        else if(!password.matches(passwordRegex)){
            return ValidationResult(success = false, message = "Incorrect password format")
        }

        return ValidationResult(success = true, message = "")
    }
}