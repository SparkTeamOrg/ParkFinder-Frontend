package com.app.parkfinder.ui.screens.auth.register

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.logic.view_models.VehicleBrandViewModel
import com.app.parkfinder.logic.view_models.VehicleModelViewModel

@Composable
fun RegisterVehicleInfoScreen(
    //used for update
    selectedBrand: Int? = null,
    selectedBrandName: String? = null,
    selectedModelName: String? = null,
    selectedColor: Int? = null,
    checkIfModified: (()->Boolean)? = null,
    image: Int? = null,
    //used for registration and add
    onSelectedBrandChange: (Int) -> Unit,
    onSelectedModelChange: (Int) -> Unit,
    onSelectedColorChange: (Int) -> Unit,
    licencePlate: String,
    onLicencePlateChange: (String) -> Unit,
    colorNames: Map<Int, String>,
    onBackClick: () -> Unit,
    viewVehicleBrandModel: VehicleBrandViewModel = viewModel(),
    viewVehicleModel: VehicleModelViewModel = viewModel(),
    register: () -> List<Boolean>
) {

    LaunchedEffect(Unit) {
        viewVehicleBrandModel.getAllVehicleBrands()
        if (selectedBrand != null) {
            viewVehicleModel.getAllVehicleModelsByBrand(selectedBrand)
        }
    }

    var brandError by remember { mutableStateOf(false) }
    var modelError by remember { mutableStateOf(false) }
    var colorError by remember { mutableStateOf(false) }
    var regNumError by remember { mutableStateOf(false) }

    var buttonEnabled by remember { mutableStateOf(true) }
    buttonEnabled = (checkIfModified != null && checkIfModified()) || checkIfModified == null

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
            text = if(selectedBrand!=null) stringResource(id = R.string.register_vehicle_info_update_vehicle_information) else stringResource(id = R.string.register_vehicle_info_setup_vehicle_information),
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
                painter = painterResource(id = image ?: R.drawable.car1),
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
                    text = stringResource(id = R.string.register_vehicle_info_vehicle_information),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier.align(Alignment.Start)
                )

                OutlinedDropdownMenu(
                    label = stringResource(id = R.string.register_vehicle_info_brand),
                    selectedText = selectedBrandName ?: stringResource(id = R.string.register_vehicle_info_select_a_brand),
                    options = viewVehicleBrandModel.brands.value,
                    icon = Icons.Default.DirectionsCar,
                    isError =  brandError,
                    onOptionSelected = { option ->
                        run {
                            onSelectedBrandChange(option)
                            viewVehicleModel.getAllVehicleModelsByBrand(option)
                        }
                        onSelectedModelChange(0)
                    }
                )

                OutlinedDropdownMenu(
                    label = stringResource(id = R.string.register_vehicle_info_model),
                    selectedText = selectedModelName ?: stringResource(id = R.string.register_vehicle_info_select_a_model),
                    options = viewVehicleModel.vehicle_models.value,
                    icon = Icons.Default.DirectionsCar,
                    isError = modelError,
                    onOptionSelected = { option -> onSelectedModelChange(option) }
                )

                OutlinedDropdownMenu(
                    label = stringResource(id = R.string.register_vehicle_info_color),
                    selectedText = colorNames[selectedColor] ?: stringResource(id = R.string.register_vehicle_info_select_a_color),
                    options = colorNames,
                    icon = Icons.Default.ColorLens,
                    isError = colorError,
                    onOptionSelected = { option -> onSelectedColorChange(option) }
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
                                text = stringResource(id = R.string.register_vehicle_info_invalid_registration_plate),
                                color = Color.Red,
                            )
                        } else {
                            Text("")
                        }
                    },
                    isError = regNumError,
                    value = licencePlate,
                    onValueChange = {
                        onLicencePlateChange(it)
                        regNumError = false
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.register_vehicle_info_registration_plate),
                            color = if (regNumError) Color.Red else White
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                val valResults = register()
                brandError = valResults[0]
                modelError = valResults[1]
                colorError = valResults[2]
                regNumError = valResults[3]
                if (regNumError){
                    onLicencePlateChange("")
                }
            },
            enabled = buttonEnabled,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0FCFFF),
                disabledContainerColor = Color(0xFF0FCFFF).copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = stringResource(id = R.string.common_finish),
                color = if (buttonEnabled) White else White.copy(alpha = 0.3f)
            )
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
    var errorOccurred by remember { mutableStateOf(isError) }

    LaunchedEffect(isError) {
        errorOccurred = isError
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = showText,
            onValueChange = {},
            isError = errorOccurred,
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
                    tint = if(errorOccurred) Color.Red else White
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = if(errorOccurred) Color.Red else White
                )
            },
            label = { Text(label, color = if (errorOccurred) Color.Red else White) },
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
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt.value, color = White) },
                    onClick = {
                        expanded = false
                        onOptionSelected(opt.key)
                        showText = opt.value
                        errorOccurred = false
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
        RegisterVehicleInfoScreen(
            onSelectedBrandChange = {},
            onSelectedModelChange = {},
            onSelectedColorChange = {},
            licencePlate = "",
            onLicencePlateChange = {},
            colorNames = mapOf(),
            onBackClick = {},
            register = { List(4){ false } }
        )
    }
}