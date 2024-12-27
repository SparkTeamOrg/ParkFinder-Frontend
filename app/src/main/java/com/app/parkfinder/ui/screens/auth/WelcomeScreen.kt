package com.app.parkfinder.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.ui.theme.ParkFinderTheme

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLanguageChange: (String) -> Unit,
    getPreferredLanguage: () -> String
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English", "Española", "Srpski")
    val flags = listOf(R.drawable.en, R.drawable.es, R.drawable.rs)
    var selectedLanguage by remember { mutableStateOf(languages[0]) }
    var selectedFlag by remember { mutableIntStateOf(flags[0]) }

    selectedLanguage = when (getPreferredLanguage()) {
        "sr" -> languages[2]
        "es" -> languages[1]
        else -> languages[0]
    }

    selectedFlag = when (getPreferredLanguage()) {
        "sr" -> flags[2]
        "es" -> flags[1]
        else -> flags[0]
    }

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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(id = R.drawable.park_finder_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(170.dp)    // Set the size of the logo
            )
            Text(
                text = stringResource(id = R.string.welcome_about),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700),
                fontSize = 16.sp,
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
                        text = stringResource(id = R.string.common_login),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
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
                        text = stringResource(id = R.string.common_register),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFF7ab3bf))
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = selectedFlag),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = selectedLanguage,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(8.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    languages.forEachIndexed { index, language ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = flags[index]),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(language)
                                }
                            },
                            onClick = {
                                selectedLanguage = language
                                expanded = false
                                val locale = when (language) {
                                    "Srpski" -> "sr"
                                    "Española" -> "es"
                                    else -> "en"
                                }
                                onLanguageChange(locale)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ParkFinderTheme {
        WelcomeScreen(
            onLoginClick = {},
            onRegisterClick = {},
            onLanguageChange = {},
            getPreferredLanguage = { "en" }
        )
    }
}