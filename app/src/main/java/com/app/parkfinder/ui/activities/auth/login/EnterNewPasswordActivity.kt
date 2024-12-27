package com.app.parkfinder.ui.activities.auth.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.ResetPasswordDto
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.screens.auth.login.EnterNewPasswordScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.TranslationHelper
import com.app.parkfinder.utilis.validatePassword

class EnterNewPasswordActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var email: String
    private lateinit var code: String

    private val password = mutableStateOf("")
    private val confirmPassword = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra("email") ?: ""
        code = intent.getStringExtra("code") ?: ""

        setContent {
            ParkFinderTheme {
                EnterNewPasswordScreen(
                    password = password.value,
                    onPasswordChange = { password.value = it },
                    confirmPassword = confirmPassword.value,
                    onConfirmPasswordChange = { confirmPassword.value = it },
                    validatePassword = { validatePassword(it) },
                    onBackClick = { finish() },
                    onFinishClick = { resetPassword() }
                )
            }
        }

        authViewModel.passwordResetResult.observe(this) { result ->
            if (result.isSuccessful) {
                val translatedMessage = TranslationHelper.getTranslatedMessage(this, "password reset successful")
                Toast.makeText(this, translatedMessage, Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
            else {
                val translatedMessage = TranslationHelper.getTranslatedMessage(this, "Password reset failed")
                Toast.makeText(this, translatedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetPassword() {
        if (validatePassword(password.value) && password.value == confirmPassword.value) {
            authViewModel.resetPassword(
                ResetPasswordDto(
                    email = email,
                    code = code,
                    newPassword = password.value
                )
            )
        }
        else {
            val translatedMessage = TranslationHelper.getTranslatedMessage(this, "Passwords are not valid format or don't match")
            Toast.makeText(this, translatedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}