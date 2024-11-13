package com.app.parkfinder.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OTPBox(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                if (newValue.length <= 1) {
                    onValueChange(newValue)
                }
                else {
                    // In case there are more than one character
                    // we take the character that differs from the current value
                    // and set it as the new value
                    val diffIndex = newValue.indexOfFirst { it != value.first() }
                    if (diffIndex != -1) {
                        onValueChange(newValue[diffIndex].toString())
                    }
                }
            }
        },
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF2E3341),
            unfocusedContainerColor = Color(0xFF2E3341),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White
        ),
        modifier = modifier
            .size(56.dp)
            .background(Color(0xFF2E3341), RoundedCornerShape(16.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun OTPBoxPreview() {
    OTPBox(
        value = "1",
        onValueChange = {},
        modifier = Modifier
    )
}