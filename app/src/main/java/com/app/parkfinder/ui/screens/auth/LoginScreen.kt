package com.app.parkfinder.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun LoginScreen(
        email: String,
        onEmailChange: (String) -> Unit,
        password: String,
        onPasswordChange: (String) -> Unit,
        onBackClick: () -> Unit,
        onForgotPasswordClick: () -> Unit,
        onRegisterClick: () -> Unit,
        login: () -> Unit,
        validateEmail: (String) -> Boolean,
        validatePassword: (String) -> Boolean
    ) {

    var passwordVisible by remember { mutableStateOf(false) }   // For toggling password visibility

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            // Dummy icon in order to align the logo center
            // Has to be transparent to not be visible
            // Has to have the same size as the back button
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Dummy",
                tint = Color.Transparent,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Enter your \ncredentials to Login",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
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
                Text(
                    text = "Login",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )
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
                                text = "Invalid format for email address",
                                color = Color.Red,
                                fontStyle = FontStyle.Italic
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = if(!emailError) email else "",
                    onValueChange = {
                        onEmailChange(it)
                        emailError = false
                    },
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
                                text = "Invalid password format",
                                color = Color.Red,
                                fontStyle = FontStyle.Italic
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = if(!passwordError) password else "",
                    onValueChange = {
                        onPasswordChange(it)
                        passwordError = false
                    },
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
                Text(
                    text = "Forgot password?",
                    color = Color.White,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clickable { onForgotPasswordClick() }
                )
                Button(
                    onClick = {
                        val isEmailValid = validateEmail(email)
                        val isPasswordValid = validatePassword(password)
                        emailError = !isEmailValid
                        passwordError = !isPasswordValid
                        if (isEmailValid && isPasswordValid) {
                            login()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF),
                        contentColor = Color.White
                    )
                ) {
                    Text("Login")
                }
            }
        }

        // Already have an account? Register
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.White
            )
            Text(
                text = "Register",
                color = Color(0xFF0FCFFF),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onRegisterClick() }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ParkFinderTheme {
        LoginScreen(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            onBackClick = {},
            onForgotPasswordClick = {},
            onRegisterClick = {},
            login = {},
            validateEmail = { true },
            validatePassword = { true }
        )
    }
}