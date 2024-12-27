package com.app.parkfinder.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.app.parkfinder.R
import com.app.parkfinder.utilis.digitCheck
import com.app.parkfinder.utilis.lengthCheck
import com.app.parkfinder.utilis.lowercaseCheck
import com.app.parkfinder.utilis.specialCharCheck
import com.app.parkfinder.utilis.uppercaseCheck

@Composable
fun PasswordHelp(password: String)
{
    var showTooltip by remember { mutableStateOf(false) }
    //popup
    Box(contentAlignment = Alignment.Center) {
        IconButton(
            onClick = { showTooltip = !showTooltip },
            modifier = Modifier,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Help,
                contentDescription = "Help Icon",
                tint = Color.White
            )
        }

        if (showTooltip) {
            Popup(
                alignment = Alignment.BottomStart,
                onDismissRequest = { }
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .size(280.dp,180.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.password_help_password_requirements),
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.size(270.dp,20.dp)
                        )
                        IconButton(
                            onClick = {showTooltip = !showTooltip},
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = Color.White,
                                contentDescription = "Close tooltip",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                    Text(
                        text = stringResource(id = R.string.password_help_requirement_1),
                        color = if(lengthCheck(password)) Color.Green else Color.Red,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(id = R.string.password_help_requirement_2),
                        color = if(uppercaseCheck(password)) Color.Green else Color.Red,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(id = R.string.password_help_requirement_3),
                        color = if(lowercaseCheck(password)) Color.Green else Color.Red,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(id = R.string.password_help_requirement_4),
                        color = if(digitCheck(password)) Color.Green else Color.Red,
                        fontSize = 14.sp
                    )
                    Text(
                        text = stringResource(id = R.string.password_help_requirement_5),
                        color = if(specialCharCheck(password)) Color.Green else Color.Red,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}