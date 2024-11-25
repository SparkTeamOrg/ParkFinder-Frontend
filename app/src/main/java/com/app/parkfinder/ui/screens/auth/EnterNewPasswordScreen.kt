package com.app.parkfinder.ui.screens.auth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.common.PasswordHelp
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun EnterNewPasswordScreen(
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    validatePassword: (String) -> Boolean,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit
)
{
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
            text = "Forgot password",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Enter your new password and confirm it",
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(300.dp)
        )
        Spacer(modifier = Modifier.height(34.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color(36, 45, 64).copy(alpha = 0.4f))
                .padding(16.dp)
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Text(
                        text = "Change password",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,

                    )
                    PasswordHelp(password)
                }
                Spacer(Modifier.padding(10.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        onPasswordChange(it)
                        passwordError = false
                    },
                    placeholder = {
                        if(passwordError) {
                            Text(
                                text = "Invalid password format",
                                color = Color.Red,
                                fontWeight = FontWeight.Light,
                                fontStyle =FontStyle.Italic
                            )
                        }
                        else {
                            Text(
                                text = "Password",
                                color = Color.White,
                                fontWeight = FontWeight.Light,
                                fontStyle =FontStyle.Italic
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Lock Icon",
                            tint = if(passwordError) Color.Red else Color.White
                        )
                    },
                    trailingIcon = {
                        val visibilityIcon = if (passwordVisible) Icons.Default.Visibility  else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = visibilityIcon,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = if (passwordError) Color.Red else Color.White
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2E3341),
                        unfocusedContainerColor = Color(0xFF2E3341),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    isError = passwordError,
                    label = { Text("Password", color = if (passwordError) Color.Red else Color.White) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        onConfirmPasswordChange(it)
                        passwordError = false
                    },
                    placeholder = {
                        if(passwordError) {
                            Text(
                                text = "Passwords don't match",
                                color = Color.Red,
                                fontWeight = FontWeight.Light,
                                fontStyle =FontStyle.Italic
                            )
                        }
                        else {
                            Text(
                                text = "Confirm Password",
                                color = Color.White,
                                fontWeight = FontWeight.Light,
                                fontStyle =FontStyle.Italic
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Lock Icon",
                            tint = if(passwordError) Color.Red else Color.White
                        )
                    },
                    trailingIcon = {
                        val visibilityIcon = if (passwordVisible) Icons.Default.Visibility  else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = visibilityIcon,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = if (passwordError) Color.Red else Color.White
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2E3341),
                        unfocusedContainerColor = Color(0xFF2E3341),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    isError = passwordError,
                    label = { Text("Confirm Password", color = if (passwordError) Color.Red else Color.White) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(Modifier.padding(20.dp))
                Button(
                    onClick = {
                        if (password != confirmPassword || !validatePassword(password)) {
                            passwordError = true
                            onPasswordChange("")
                            onConfirmPasswordChange("")
                        }
                        else {
                            passwordError = false
                            onFinishClick()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Finish",
                        fontSize = 24.sp,
                    )
                }
                Spacer(Modifier.padding(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnterNewPasswordScreenPreview() {
    ParkFinderTheme {
        EnterNewPasswordScreen(
            password = "",
            onPasswordChange = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            validatePassword = { true },
            onBackClick = {},
            onFinishClick = {}
        )
    }
}