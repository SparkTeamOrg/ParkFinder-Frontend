package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R

@Composable
fun SettingsScreen(
    onLanguageChange: (String) -> Unit,
    onBackClick: () -> Unit,
    getPreferredLanguage: () -> String
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English", "Española", "Srpski")
    val flags = listOf(R.drawable.en, R.drawable.es, R.drawable.rs)
    var selectedLanguage by remember { mutableStateOf(languages[0]) }
    var selectedFlag by remember { mutableStateOf(flags[0]) }

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
                onClick = { onBackClick() },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF293038), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = White
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

        Text(
            text = "Settings",
            fontSize = 24.sp,
            color = White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Selected Language",
            fontSize = 18.sp,
            color = White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
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
                            selectedFlag = flags[index]
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