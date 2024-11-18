package com.app.parkfinder.ui.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.parkfinder.R

@Composable
fun ParkFinderLogo()
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF151A24)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Dummy",
            tint = Color.Transparent,
            modifier = Modifier.size(60.dp)
        )
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
}

@Preview(showBackground = true)
@Composable
fun ParkFinderLogoPreview()
{
    ParkFinderLogo()
}