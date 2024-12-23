package com.app.parkfinder.ui.screens.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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
import com.app.parkfinder.R
import com.app.parkfinder.ui.composables.PasswordHelp
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun RegisterScreen(
    email: String = "",
    onEmailChange: (String) -> Unit,
    password: String = "",
    onPasswordChange: (String) -> Unit,
    confirmedPassword: String = "",
    onConfirmedPasswordChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onNextClick: (String) -> Unit,
    validateEmail: (String) -> Boolean,
    validatePassword: (String) -> Boolean
) {
    val invalidPasswordFormat = stringResource(id = R.string.error_invalid_password_format)
    val passwordsDoNotMatch = stringResource(id = R.string.error_passwords_do_not_match)
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    val annotatedText = buildAnnotatedString {
        append(stringResource(id = R.string.register_already_have_account) + " ")
        pushStringAnnotation(tag = "URL", annotation = "login")
        withStyle(style = SpanStyle(color = Color(0xFF0FCFFF), textDecoration = TextDecoration.Underline, fontSize = 16.sp)) {
            append(stringResource(id = R.string.common_login))
        }
        pop()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Top bar
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
            text = stringResource(id = R.string.register_enter_email_and_password),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.register_credentials),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    PasswordHelp(password)
                }
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
                                text = stringResource(id = R.string.error_invalid_email_format),
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = email,
                    onValueChange = {
                        onEmailChange(it)
                        emailError = false
                    },
                    isError = emailError,
                    label = {
                        Text(
                            text = stringResource(id = R.string.common_email),
                            color = if (emailError) Color.Red else Color.White )
                    },
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
                                text = passwordErrorMessage,
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = password,
                    onValueChange = {
                        onPasswordChange(it)
                        passwordError = false
                    },
                    isError = passwordError,
                    label = {
                        Text(
                            text = stringResource(id = R.string.common_password),
                            color = if (passwordError) Color.Red else Color.White)
                    },
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
                                text = passwordErrorMessage,
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    value = confirmedPassword,
                    onValueChange = {
                        onConfirmedPasswordChange(it)
                        passwordError = false
                    },
                    isError = passwordError,
                    label = {
                        Text(
                            text = stringResource(id = R.string.enter_new_password_confirm_password),
                            color = if (passwordError) Color.Red else Color.White)
                    },
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
                        emailError = !validateEmail(email)
                        val passwordFormatError = !validatePassword(password)
                        val passwordMatchError = password != confirmedPassword

                        passwordError = passwordFormatError || passwordMatchError
                        passwordErrorMessage = when{
                            passwordFormatError -> invalidPasswordFormat
                            passwordMatchError -> passwordsDoNotMatch
                            else -> {""}
                        }

                        if(!emailError && !passwordError) {
                            onNextClick(email)
                        }else{
                            if(passwordError){
                                onPasswordChange("")
                                onConfirmedPasswordChange("")
                            }
                            if(emailError){
                                onEmailChange("")
                            }
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
                        text = stringResource(id = R.string.common_next)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = annotatedText,
                modifier = Modifier.clickable { onLoginClick() },
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ParkFinderTheme {
        RegisterScreen(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            confirmedPassword = "",
            onConfirmedPasswordChange = {},
            onBackClick = {},
            onLoginClick = {},
            validateEmail = { true },
            validatePassword = { true },
            onNextClick = {}
        )
    }
}