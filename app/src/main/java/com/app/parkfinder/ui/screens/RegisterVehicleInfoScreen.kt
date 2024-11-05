package com.app.parkfinder.ui.screens

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.logic.models.dtos.UserRegisterDto
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.logic.view_models.VehicleBrandViewModel
import com.app.parkfinder.logic.view_models.VehicleModelViewModel
import com.app.parkfinder.ui.ValidationResult

@Composable
fun RegisterUserDataScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    isRegisterNumberValid: (String) -> ValidationResult,
    viewVehicleBrandModel: VehicleBrandViewModel = viewModel(),
    viewVehicleModel: VehicleModelViewModel = viewModel(),
    viewAuth: AuthViewModel = viewModel(),
    activityIntent: Intent
) {
    LaunchedEffect(Unit) {
        viewVehicleBrandModel.getAllVehicleBrands()
    }

    var email = activityIntent.getStringExtra("email")!!
    var fs = activityIntent.getStringExtra("firstname")!!
    var ls = activityIntent.getStringExtra("lastname")!!
    var pass = activityIntent.getStringExtra("password")!!
    var code = activityIntent.getStringExtra("verificationCode")!!
    var phone = activityIntent.getStringExtra("phone")!!
    var profileImage =activityIntent.getStringExtra("profilePicture")!!

    var selectedBrand by remember { mutableIntStateOf(0) }
    var selectedModel by remember { mutableIntStateOf(0) }
    var selectedColor by remember { mutableIntStateOf(0) }
    var registrationNumber by remember { mutableStateOf("") }

    var brandError by remember { mutableStateOf(false) }
    var modelError by remember { mutableStateOf(false) }
    var colorError by remember { mutableStateOf(false) }
    var regNumError by remember { mutableStateOf(false) }
    var regNumValidation by remember { mutableStateOf(ValidationResult()) }

    val colorNames = mapOf(1 to "Red",
        2 to "Green",
        3 to "Blue",
        4 to "Yellow",
        5 to "Cyan",
        6 to "Magenta",
        7 to "Gray",
        8 to "Black"
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
                    options = viewVehicleBrandModel.brands.value,
                    icon = Icons.Default.DirectionsCar,
                    isError =  brandError,
                    onOptionSelected = {
                        option ->
                        run {
                            selectedBrand = option
                            viewVehicleModel.getAllVehicleModelsByBrand(option)
                        }
                    }
                )
                OutlinedDropdownMenu(
                    label = "Model",
                    selectedText = "Select a model",
                    options = viewVehicleModel.vehicle_models.value,
                    icon = Icons.Default.DirectionsCar,
                    isError = modelError,
                    onOptionSelected = { option -> selectedModel = option }
                )
                OutlinedDropdownMenu(
                    label = "Color",
                    selectedText = "Select a color",
                    options = colorNames,
                    icon = Icons.Default.ColorLens,
                    isError = colorError,
                    onOptionSelected = { option -> selectedColor = option }
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
                            tint = if(regNumError) Color.Red else White
                        )
                    },
                    placeholder = {
                        if (regNumError) {
                            Text(
                                text = regNumValidation.message,
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    isError = regNumError,
                    value = registrationNumber,
                    onValueChange = {
                        registrationNumber = it
                        regNumError = false },
                    label = { Text("Registration Number", color = if (regNumError) Color.Red else White) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                brandError = selectedBrand == 0
                modelError = selectedModel == 0
                colorError = selectedColor == 0
                regNumValidation = isRegisterNumberValid(registrationNumber)

                if (!brandError && !modelError && !colorError && regNumValidation.success) {
                    onNextClick()
                    viewAuth.register(UserRegisterDto(
                        email = email,
                        password = pass,
                        firstName = fs,
                        mobilePhone = phone,
                        profileImage = profileImage,
                        lastName = ls,
                        licencePlate = registrationNumber,
                        color = colorNames.get(selectedColor)!!,
                        modelId = selectedBrand,
                        verificationCode = code,
                    ))
                }
                else{
                    if (!regNumValidation.success){
                        registrationNumber = ""
                        regNumError = true
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0FCFFF),
                contentColor = White
            )
        ) {
            Text("Finish")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDropdownMenu(
    label: String,
    selectedText: String,
    options: Map<Int,String>,
    icon: ImageVector,
    isError: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(selectedText) }
    var errorOccured by remember { mutableStateOf(isError) }


    LaunchedEffect(isError) {
        errorOccured = isError
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = showText,
            onValueChange = {},
            isError = errorOccured,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(36, 45, 64),
                unfocusedBorderColor = White,
                unfocusedTextColor = White,
                focusedTextColor = White,
                errorTextColor = Color.Red
            ),
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Car Icon",
                    tint = if(errorOccured) Color.Red else White
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = if(errorOccured) Color.Red else White
                )
            },
            label = { Text(label, color = if (errorOccured) Color.Red else White) },
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
                    text = { Text(option.value, color = White) },
                    onClick = {
                        expanded = false
                        onOptionSelected(option.key)
                        showText = option.value
                        errorOccured = false
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
        val checkNumber : (String) -> ValidationResult = { ValidationResult() }
        RegisterUserDataScreen(
            onBackClick = {},
            onNextClick = {},
            isRegisterNumberValid = checkNumber,
            activityIntent = Intent()
        )
    }
}