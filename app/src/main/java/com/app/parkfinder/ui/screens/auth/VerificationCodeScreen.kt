package com.app.parkfinder.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.ui.composables.OTPBox
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun VerificationCodeScreen(
    email: String = "",
    otpValues:  List<String> = List(4) { "" },
    onOtpValueChange: (List<String>) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onResendClick: () -> Unit
) {
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
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Dummy",
                tint = Color.Transparent,
                modifier = Modifier.size(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Verification Code",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We've sent the code to your mail address that you provided:",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Text(
            text = email,
            fontSize = 16.sp,
            color = Color(0xFF0FCFFF),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { /* Handle email click */ }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // OTP Input Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            otpValues.forEachIndexed { index, _ ->
                OTPBox(
                    value = otpValues[index],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            val newOtpValues = otpValues.toMutableList()
                            newOtpValues[index] = newValue
                            onOtpValueChange(newOtpValues)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Resend Button
        Text(
            text = "Resend",
            fontSize = 16.sp,
            color = Color(0xFF0FCFFF),
            modifier = Modifier.clickable { onResendClick() },
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(40.dp))

        // Next Button
        Button(
            onClick = onNextClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0FCFFF),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Next",
                fontSize = 20.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationCodeScreenPreview() {
    ParkFinderTheme {
        VerificationCodeScreen(
            email = "email@example.com",
            otpValues = List(4) { "" },
            onOtpValueChange = {},
            onBackClick = {},
            onNextClick = {},
            onResendClick = {}
        )
    }
}