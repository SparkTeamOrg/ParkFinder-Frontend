package com.app.parkfinder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.ui.theme.ParkFinderTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType

@Composable
fun RegisterUserDataScreen(
    onBackClick: () -> Unit,
) {
//    var selectedBrand by remember { mutableStateOf("") }
//    var selectedModel by remember { mutableStateOf("") }
//    var selectedColor by remember { mutableStateOf("") }
    var licencePlate by remember { mutableStateOf("") }
    val colorNames = mapOf(Color.Red to "red",
        Color.Green to "green",
        Color.Blue to "blue",
        Color.Yellow to "yellow",
        Color.Cyan to "cyan",
        Color.Magenta to "magenta",
        Color.Gray to "gray",
        Color.Black to "black"
    )

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
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Please set up your vehicle",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color(0xFF0FCFFF), shape = CircleShape)
                .background(White)
        ){
            Image(
                painter = painterResource(id = R.drawable.car),
                contentDescription = "Background Image",
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .align(Alignment.Center),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(8.dp))
                .background(Color(36, 45, 64).copy(alpha = 0.4f))
                .padding(16.dp)
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Vehicle Informations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedDropdownMenu(
                    label = "Brand",
                    selectedText = "Select a brand",
                    options = listOf("audi", "bmw"),
                    icon = Icons.Default.DirectionsCar,
                )
                OutlinedDropdownMenu(
                    label = "Model",
                    selectedText = "Select a model",
                    options = listOf("A3", "X5"),
                    icon = Icons.Default.DirectionsCar,
                )
                OutlinedDropdownMenu(
                    label = "Color",
                    selectedText = "Select a color",
                    options = colorNames.values.toList(),
                    icon = Icons.Default.ColorLens,
                )
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(36, 45, 64),
                        unfocusedBorderColor = White,
                        unfocusedTextColor = White,
                        focusedTextColor = White
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AddCard,
                            contentDescription = "plateIcon",
                            tint = White
                        )
                    },
                    value = licencePlate,
                    onValueChange = { licencePlate = it },
                    label = { Text("Registration Number", color = White) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {

            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0FCFFF),
                contentColor = White
            )
        ) {
            Text("Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDropdownMenu(
    label: String,
    selectedText: String,
    options: List<String>,
    icon: ImageVector,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = {},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(36, 45, 64),
                unfocusedBorderColor = White,
                unfocusedTextColor = White,
                focusedTextColor = White
            ),
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Car Icon",
                    tint = White
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = White
                )
            },
            label = { Text(label, color = White) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(36, 45, 64))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = White) },
                    onClick = {
                        expanded = false
                        selectedOption = option
                    },
                    modifier = Modifier.background(Color(36, 45, 64))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterVehicleInfoScreenPreview() {
    ParkFinderTheme {
        RegisterUserDataScreen(
            onBackClick = {},
        )
    }
}