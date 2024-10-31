package com.app.parkfinder.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.app.parkfinder.ui.theme.ParkFinderTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.app.parkfinder.R

class WelcomeActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                WelcomeScreen(
                    onLoginClick = { navigateToLogin() },
                    onRegisterClick = { navigateToRegister() }
                )
            }
        }
    }

    private fun navigateToLogin() {
        // Navigate to login screen
    }

    private fun navigateToRegister() {
        // Navigate to register screen
    }
}

// Composable function that displays a welcome screen with login and register buttons.
@Composable
fun WelcomeScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(id = R.drawable.park_finder_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(170.dp)    // Set the size of the logo
            )
            // Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Your digital assistant \n for finding parking lots fast",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700),
                fontSize = 20.sp,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(4.dp),
                    clip = false
                )
            )
            Spacer(modifier = Modifier.height(250.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onLoginClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 150.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Login",
                        fontSize = 20.sp
                    )
                }
                Button(
                    onClick = onRegisterClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 150.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0FCFFF),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Register",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ParkFinderTheme {
        WelcomeScreen(onLoginClick = {}, onRegisterClick = {})
    }
}