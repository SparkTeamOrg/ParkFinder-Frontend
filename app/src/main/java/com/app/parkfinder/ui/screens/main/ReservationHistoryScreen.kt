package com.app.parkfinder.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationHistoryScreen(
    onBackClick: () -> Unit
) {
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
                text = "Reservation History",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FilterBar()
            ReservationHistoryItem("Zmaj Jovina",
                "Kragujvac",
                4,
                "08/11/2024 3:15",
                450.69
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterBar() {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color(0xFF00AEEF),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f
                    )
                }
                .padding(vertical = 8.dp)
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isExpanded) "Collapse Filters" else "Expand Filters",
                color = White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Expand/Collapse",
                tint = White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                OutlinedDropdownMenu(
                    label = "Select a vehicle",
                    selectedText = "",
                    options = emptyMap(),
                    icon = Icons.Default.DirectionsCar,
                    isError = false,
                    onOptionSelected = {}
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DatePicker("Newer than")
                    DatePicker("Earlier than")
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SortByGroup()
                    Column(
                        modifier = Modifier.padding(start = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AscDscGroup()
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonColors(
                                containerColor = Color(0xFF00AEEF),
                                contentColor = White,
                                disabledContentColor = White,
                                disabledContainerColor = White
                            ),
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Text(text = "Apply")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationHistoryItem(
    road: String,
    city: String,
    rating: Int,
    date: String,
    price: Double) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2A2A))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(White, shape = RoundedCornerShape(50))
                    .padding(8.dp)
            ) {
                Text(
                    text = "P",
                    color = Gray,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = road,
                        fontSize = 18.sp,
                        color = White
                    )
                    Text(
                        text = "03:15 - 05:00",
                        fontSize = 14.sp,
                        color = White
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = city,
                        fontSize = 14.sp,
                        color = Gray
                    )
                    Text(
                        text = "$price rsd",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (rating > index) Color.Yellow else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "08/11/2024",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }
        }
    }
}

@Composable
fun AscDscGroup() {
    var selectedOption by remember { mutableStateOf("Option 1") }

    Box(
        modifier = Modifier
            .border(1.dp, White, RoundedCornerShape(8.dp))
            .width(170.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            RadioButtonWithTitle(
                text = "Ascending",
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = "Ascending" }
            )
            RadioButtonWithTitle(
                text = "Descending",
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = "Descending" }
            )
        }
    }
}

@Composable
fun SortByGroup() {
    var selectedOption by remember { mutableStateOf("Option 1") }

    Box(
        modifier = Modifier
            .border(1.dp, White, RoundedCornerShape(8.dp))
            .width(170.dp),
    ) {
        Text(
            text = "Sort by",
            modifier = Modifier
                .padding(start = 16.dp, top = 5.dp)
                .align(Alignment.TopStart),
            color = White
        )

        Column(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 10.dp)
        ) {
            RadioButtonWithTitle(
                text = "Date",
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = "Date" }
            )
            RadioButtonWithTitle(
                text = "Rating",
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = "Rating" }
            )
            RadioButtonWithTitle(
                text = "Price",
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = "Price" }
            )
        }
    }
}

@Composable
fun RadioButtonWithTitle(
    text: String,
    selectedOption: String,
    onOptionSelected: () -> Unit
) {
    Row(
        modifier = Modifier.height(35.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selectedOption == text,
            onClick = onOptionSelected,
            colors = RadioButtonDefaults.colors(
                unselectedColor = Color(0xFF00AEEF),
                selectedColor = Color(0xFF00AEEF)
            )
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = White
        )
    }
}

@Composable
fun DatePicker(
    text: String
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text(
            text = text,
            color = White
        ) },
        trailingIcon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = White
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = White,
            unfocusedBorderColor = White,
            focusedTextColor = White
        ),
        modifier = Modifier
            .width(170.dp)
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
            .padding(top = 10.dp)
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = White,
                    containerColor = Color(0xFF00AEEF)
                )
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = White,
                    containerColor = Color.Red
                )
            ) {
                Text("Cancel")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFF293038)
        ),
        modifier = Modifier.size(550.dp)
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF151A24),
                titleContentColor = White,
                headlineContentColor = White,
                weekdayContentColor = Gray,
                subheadContentColor = White,
                navigationContentColor= White,
                yearContentColor = White,
                currentYearContentColor = White,
                selectedYearContentColor = White,
                selectedYearContainerColor = Color(0xFF00AEEF),
                dividerColor = Gray,
                dayContentColor = White,
                todayContentColor = Color(0xFF00AEEF),
                todayDateBorderColor = Color(0xFF00AEEF),
                selectedDayContainerColor = Color(0xFF00AEEF),
            ),
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
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
                unfocusedContainerColor = Color(0xFF151A24),
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ReservationHistoryPreview() {
    ReservationHistoryScreen(
        onBackClick = {}
    )
}