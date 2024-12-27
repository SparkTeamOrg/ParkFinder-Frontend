package com.app.parkfinder.ui.screens.main

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import androidx.compose.foundation.Canvas
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize


@Composable
fun HelpCenterScreen(
    onBackClick: () -> Unit
) {
    val bitmap: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.landline)

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

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = stringResource(id = R.string.help_center_title),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
                    .align(Alignment.Center)
                    .offset(x = 130.dp, y = 300.dp)
            ) {
                drawCircle(
                    color = Color(0xFF1D2535).copy(alpha = 0.5f),
                    radius = 800f,
                    center = center
                )

                val canvasWidth = size.width.toInt()
                val canvasHeight = size.height.toInt()
                val imageHeight = bitmap.height
                val imageWidth = bitmap.width
                val offsetX = (canvasWidth - imageWidth) / 2
                val offsetY = (canvasHeight - imageHeight) / 2
                val srcOffset = IntOffset(0, 0)
                val dstOffset = IntOffset(offsetX+100, offsetY+20)

                drawImage(
                    image = bitmap,
                    srcOffset = srcOffset,
                    dstOffset = dstOffset,
                    srcSize = IntSize(imageWidth, imageHeight),
                    dstSize = IntSize(1000, 800),
                    colorFilter = ColorFilter.tint(Color(0xFF151A24))
                )
            }
        }
        Text(
            text = stringResource(id = R.string.help_center_need_assistance) + "\n" + stringResource(id = R.string.help_center_we_are_here_to_help),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = White.copy(alpha = 0.6f),
            lineHeight = 30.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 180.dp, start = 15.dp)
        )

        Spacer(modifier = Modifier.height(230.dp))

        Text(
            text = stringResource(id = R.string.help_center_contact_email),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
        )
        Divider(
            color = White.copy(alpha = 0.3f),
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "support@parkfinder.com",
            fontSize = 16.sp,
            color = Color(0xFF00AEEF),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.help_center_contact_phone),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
        )
        Divider(
            color = White.copy(alpha = 0.3f),
            thickness = 2.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "mob: +381123456789\nmob: +381123456342\nmob: +381123456171",
            fontSize = 16.sp,
            color = Color(0xFF00AEEF),
            lineHeight = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
        )
    }
}