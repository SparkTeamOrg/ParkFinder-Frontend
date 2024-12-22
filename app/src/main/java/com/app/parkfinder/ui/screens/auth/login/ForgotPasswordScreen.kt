package com.app.parkfinder.ui.screens.auth.login

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
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validateEmail

@Composable
fun ForgotPasswordScreen (
    email: String,
    onEmailChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit
)
{
    var emailError by remember { mutableStateOf(false) }

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
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = stringResource(id = R.string.forgot_password_forgot_password),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.forgot_password_hint),
            fontSize = 20.sp,
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
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.common_email),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Start)

                )
                Spacer(Modifier.padding(10.dp))
                OutlinedTextField(
                    value = if(!emailError) email else "",
                    onValueChange = {
                        onEmailChange(it)
                        emailError = false },
                    placeholder = {
                        if(emailError){
                            Text(
                                text = stringResource(id = R.string.error_invalid_email_format),
                                color = Color.Red,
                                fontStyle = FontStyle.Italic
                            )
                        }
                        else{
                            Text(
                                text = stringResource(id = R.string.forgot_password_enter_email),
                                color = Color.White,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email Icon",
                            tint = if(emailError) Color.Red else Color.White
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2E3341), // Background color when focused
                        unfocusedContainerColor = Color(0xFF2E3341), // Background color when not focused
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    isError = emailError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(Modifier.padding(20.dp))
                Button(
                    onClick = {
                        emailError = !validateEmail(email)
                        if(!emailError){
                            onSendClick()
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
                        text = stringResource(id = R.string.forgot_password_send_verification_code),
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
fun ForgotPassScreenPreview() {
    ParkFinderTheme {
        ForgotPasswordScreen(
            email = "",
            onEmailChange = {},
            onBackClick = {},
            onSendClick = {}
        )
    }
}