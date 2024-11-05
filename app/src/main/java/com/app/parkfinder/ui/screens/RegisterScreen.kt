package com.app.parkfinder.ui.screens

import android.app.ActivityOptions
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.MainActivity
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.ValidationResult
import com.app.parkfinder.ui.activities.RegisterActivity
import com.app.parkfinder.ui.activities.VerificationCodeActivity
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onNextClick: () -> Unit,
    isValidEmail: (String) -> ValidationResult,
    isValidPassword: (String, String) -> ValidationResult,
    viewModel: AuthViewModel = viewModel()
) {
    var context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var emailValidation by remember { mutableStateOf(ValidationResult()) }
    var passwordValidation by remember { mutableStateOf(ValidationResult()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF293038), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.park_finder_logo),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Dummy",
                tint = Color.Transparent,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Enter your email and \npassword",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(60.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color(36, 45, 64).copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "Credentials",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(0.5.dp))
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(36, 45, 64),
                        unfocusedBorderColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email,
                            contentDescription = "emailIcon",
                            tint = if(emailError) Color.Red else Color.White) },
                    placeholder = {
                        if (emailError) {
                            Text(
                                text = emailValidation.message,
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false },
                    isError = emailError,
                    label = { Text("Email", color = if (emailError) Color.Red else Color.White ) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(36, 45, 64),
                        unfocusedBorderColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock,
                            contentDescription = "LockIcon",
                            tint = if(passwordError) Color.Red else Color.White) },
                    placeholder = {
                        if (passwordError) {
                            Text(
                                text = passwordValidation.message,
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false},
                    isError = passwordError,
                    label = { Text("Password", color = if (passwordError) Color.Red else Color.White) },
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (password.isNotEmpty()) {
                            val image =
                                if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = image),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(36, 45, 64),
                        unfocusedBorderColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock,
                            contentDescription = "LockIcon",
                            tint = if(passwordError) Color.Red else Color.White) },
                    placeholder = {
                        if (passwordError) {
                            Text(
                                text = passwordValidation.message,
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = confirmedPassword,
                    onValueChange = {
                        confirmedPassword = it
                        passwordError = false},
                    isError = passwordError,
                    label = { Text("Confirm password", color = if (passwordError) Color.Red else Color.White) },
                    shape = RoundedCornerShape(10.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (password.isNotEmpty()) {
                            val image =
                                if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = image),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Button(
                    onClick = {
                        emailValidation = isValidEmail(email)
                        passwordValidation = isValidPassword(password, confirmedPassword)
                        if(emailValidation.success && passwordValidation.success) {
                            viewModel.verifyEmail(email){ response ->
                                if(response.isSuccessful) {
                                    val intent = Intent(context, VerificationCodeActivity::class.java).apply {
                                        putExtra("email", email)
                                        putExtra("password", password)
                                        putExtra("verificationCode", response.data)
                                    }
                                    val options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left)
                                    context.startActivity(intent, options.toBundle())
                                }
                            }
                        } else{
                            if(!emailValidation.success){
                                email = ""
                                emailError = true
                            }
                            if(!passwordValidation.success) {
                                password = ""
                                confirmedPassword = ""
                                passwordError = true
                            }
                        }},
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF),
                        contentColor = Color.White
                    )
                ) {
                    Text("Next")
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Already have an account? ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
            )

            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF0FCFFF), textDecoration = TextDecoration.Underline, fontSize = 16.sp)) {
                        append("Login")
                    }
                },
                onClick = { onLoginClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ParkFinderTheme {
        val isEmailValid: (String) -> ValidationResult = { ValidationResult() }
        val isPasswordValid: (String, String) -> ValidationResult = { _, _ -> ValidationResult() }

        RegisterScreen(
            onBackClick = {},
            onLoginClick = {},
            isValidEmail = isEmailValid,
            isValidPassword = isPasswordValid,
            onNextClick = {}
        )
    }
}