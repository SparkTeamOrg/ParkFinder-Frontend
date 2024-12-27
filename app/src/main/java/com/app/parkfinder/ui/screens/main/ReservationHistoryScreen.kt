package com.app.parkfinder.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.ReservationHistoryItemDto
import com.app.parkfinder.logic.models.dtos.VehicleDto
import com.app.parkfinder.utilis.formatDate
import com.app.parkfinder.utilis.formatDateFirstDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationHistoryScreen(
    vehicles: List<VehicleDto>,
    onSelectedStartDateChanged: (String?) -> Unit,
    onSelectedEndDateChanged: (String?) -> Unit,
    onSelectedVehicleIdChanged: (Int?) -> Unit,
    onSelectedSortOptionChanged: (String) -> Unit,
    onSelectedAscDscOptionChanged: (String) -> Unit,
    selectedSortOption: String,
    selectedAscDscOption: String,
    selectedVehicleId: Int?,
    selectedStartDate: String?,
    selectedEndDate: String?,
    reservationHistories: LazyPagingItems<ReservationHistoryItemDto>,
    applyFilters: () -> Unit,
    onBackClick: () -> Unit,
    resetFilters: () -> Unit
) {
    val isHistoryLoading = reservationHistories.loadState.refresh is LoadState.Loading ||reservationHistories.loadState.append is LoadState.Loading

    val lazyListState = rememberLazyListState()
    var scrollToTop by remember { mutableStateOf(false) }
    val startDateInfo = selectedStartDate ?: ""
    val endDateInfo = selectedEndDate ?: ""
    val selectedVehicle = vehicles.find { x -> x.id == selectedVehicleId }
    var vehicleInfo by remember { mutableStateOf("") }

    vehicleInfo = if(selectedVehicle != null){
        selectedVehicle.vehicleModelVehicleBrandName + " " +
        selectedVehicle.vehicleModelName + " " +
        selectedVehicle.licencePlate
    } else ""

    LaunchedEffect(scrollToTop) {
        if(scrollToTop){
            if (reservationHistories.itemCount > 0) {
                lazyListState.animateScrollToItem(0)
            }
            scrollToTop = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF151A24))
            .padding(16.dp)
            .zIndex(1f),
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
            contentAlignment = Center
        ){
            Text(
                text = stringResource(id = R.string.reservation_history_title),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        FilterBar(
            vehicles = vehicles,
            onSelectedStartDateChanged = onSelectedStartDateChanged,
            onSelectedEndDateChanged = onSelectedEndDateChanged,
            onSelectedVehicleIdChanged = onSelectedVehicleIdChanged,
            onSelectedSortOptionChanged = onSelectedSortOptionChanged,
            onSelectedAscDscOptionChanged = onSelectedAscDscOptionChanged,
            selectedSortOption = selectedSortOption,
            selectedAscDscOption = selectedAscDscOption,
            applyFilters = applyFilters,
            startDateInfo = startDateInfo,
            endDateInfo = endDateInfo,
            vehicleInfo = vehicleInfo,
            resetFilters = resetFilters,
            onSelectedTextChanged = { text -> vehicleInfo = text },
            setScrollToTop = { flag -> scrollToTop = flag }
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize()
        ) {

            items(count = reservationHistories.itemCount) { index ->
                val item = reservationHistories[index]

                if (item != null) {
                    ReservationHistoryItem(
                        road = item.parkingSpotParkingLotRoad,
                        city = item.parkingSpotParkingLotCity,
                        rating = item.rating,
                        price = item.price,
                        startDate = item.startTime,
                        endDate = item.endTime,
                        vehicleInfo = "${item.vehicleVehicleModelVehicleBrandName} ${item.vehicleVehicleModelName} ${item.vehicleLicencePlate}"
                    )
                }
            }

            item {
                if (isHistoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(200.dp),
                        contentAlignment = Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(top = 100.dp)
                        )
                    }
                }
                else if(reservationHistories.itemCount == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Center)
                                .wrapContentSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(id = R.string.reservation_history_no_reservations),
                                fontSize = 18.sp,
                                color = Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = "No Reservations",
                                modifier = Modifier.size(200.dp),
                                tint = Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterBar(
    vehicles: List<VehicleDto>,
    onSelectedStartDateChanged: (String?) -> Unit,
    onSelectedEndDateChanged: (String?) -> Unit,
    onSelectedVehicleIdChanged: (Int?) -> Unit,
    onSelectedSortOptionChanged: (String) -> Unit,
    onSelectedAscDscOptionChanged: (String) -> Unit,
    startDateInfo: String,
    endDateInfo: String,
    vehicleInfo: String,
    selectedSortOption: String,
    selectedAscDscOption: String,
    applyFilters: () -> Unit,
    resetFilters: () -> Unit,
    onSelectedTextChanged: (String) -> Unit,
    setScrollToTop: (Boolean) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
            .zIndex(1f)
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
                text = if (isExpanded) stringResource(id = R.string.reservation_history_collapse_filters) else stringResource(id = R.string.reservation_history_expand_filters),
                color = White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = if (!isExpanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp,
                contentDescription = "Expand/Collapse",
                tint = White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Column {
                VehicleDropdownMenu(
                    label = stringResource(id = R.string.reservation_history_select_a_vehicle),
                    selectedText = vehicleInfo,
                    options = vehicles,
                    icon = Icons.Default.DirectionsCar,
                    onOptionSelected = onSelectedVehicleIdChanged,
                    onSelectedTextChanged = onSelectedTextChanged
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DatePicker(
                        selectedDate = startDateInfo,
                        showText = stringResource(id = R.string.reservation_history_newer_than),
                        onDateSelected = onSelectedStartDateChanged
                    )
                    DatePicker(
                        selectedDate = endDateInfo,
                        showText = stringResource(id = R.string.reservation_history_earlier_than),
                        onDateSelected = onSelectedEndDateChanged
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SortByGroup(
                        selectedOption = selectedSortOption,
                        onSelectedOptionChanged = onSelectedSortOptionChanged
                    )
                    AscDscGroup(
                        selectedOption = selectedAscDscOption,
                        onSelectedOptionChanged = onSelectedAscDscOptionChanged
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        onClick = { resetFilters() },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonColors(
                            containerColor = Color(0xFF00AEEF),
                            contentColor = White,
                            disabledContentColor = White,
                            disabledContainerColor = White
                        ),
                        modifier = Modifier.padding(top = 10.dp)
                            .width(170.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.reservation_history_reset_filters)
                        )
                    }
                    Button(
                        onClick = {
                            applyFilters()
                            setScrollToTop(true)
                            isExpanded = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonColors(
                            containerColor = Color(0xFF00AEEF),
                            contentColor = White,
                            disabledContentColor = White,
                            disabledContainerColor = White
                        ),
                        modifier = Modifier.padding(top = 10.dp)
                            .width(170.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.reservation_history_apply)
                        )
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
    startDate: String,
    endDate: String,
    vehicleInfo: String,
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
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        text = "${price.toInt()} RSD",
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = city,
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(id = R.string.reservation_history_your_review) + " ",
                        fontSize = 14.sp,
                        color = White
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (rating > index) Color.Yellow else Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(id = R.string.reservation_history_vehicle) + " ",
                        fontSize = 14.sp,
                        color = White
                    )
                    Text(
                        text = vehicleInfo,
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(id = R.string.reservation_history_time_span) + " ",
                        fontSize = 14.sp,
                        color = White
                    )
                    Text(
                        text = formatDateFirstDate(startDate) + " - ",
                        fontSize = 14.sp,
                        color = Gray
                    )
                    Text(
                        text = formatDate(endDate),
                        fontSize = 14.sp,
                        color = Gray
                    )
                }
            }
        }
    }
}

@Composable
fun AscDscGroup(
    selectedOption: String,
    onSelectedOptionChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .border(1.dp, White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .width(170.dp)
            .height(145.dp),
    ) {
        Text(
            text = stringResource(id = R.string.reservation_history_order),
            modifier = Modifier
                .padding(start = 16.dp, top = 5.dp)
                .align(Alignment.TopStart),
            color = White
        )
        Column(
            modifier = Modifier.padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            RadioButtonWithTitle(
                text = stringResource(id = R.string.reservation_history_order_ascending),
                selectedOption = selectedOption,
                onOptionSelected = { onSelectedOptionChanged("Ascending") }
            )
            RadioButtonWithTitle(
                text = stringResource(id = R.string.reservation_history_order_descending),
                selectedOption = selectedOption,
                onOptionSelected = { onSelectedOptionChanged("Descending") }
            )
        }
    }
}

@Composable
fun SortByGroup(
    selectedOption: String,
    onSelectedOptionChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .border(1.dp, White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .width(170.dp),
    ) {
        Text(
            text = stringResource(id = R.string.reservation_history_sort_by),
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
                text = stringResource(id = R.string.reservation_history_sort_by_date),
                selectedOption = selectedOption,
                onOptionSelected = { onSelectedOptionChanged("Date") }
            )
            RadioButtonWithTitle(
                text = stringResource(id = R.string.reservation_history_sort_by_rating),
                selectedOption = selectedOption,
                onOptionSelected = { onSelectedOptionChanged("Rating") }
            )
            RadioButtonWithTitle(
                text = stringResource(id = R.string.reservation_history_sort_by_price),
                selectedOption = selectedOption,
                onOptionSelected = { onSelectedOptionChanged("Price") }
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
    selectedDate: String,
    showText: String,
    onDateSelected: (String?) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text(
            text = showText,
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
            unfocusedBorderColor = White.copy(alpha = 0.3f),
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
            onDateSelected = {
                onDateSelected(it?.let { it1 -> convertMillisToDate(it1) })
            },
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
                Text(
                    text = stringResource(id = R.string.common_ok)
                )
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
                Text(
                    text = stringResource(id = R.string.common_cancel)
                )
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
fun VehicleDropdownMenu(
    label: String,
    selectedText: String,
    options: List<VehicleDto>,
    icon: ImageVector,
    onOptionSelected: (Int) -> Unit,
    onSelectedTextChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = {},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF151A24),
                unfocusedBorderColor = White.copy(alpha = 0.3f),
                unfocusedTextColor = White,
                focusedTextColor = White,
                errorTextColor = Color.Red
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
            options.forEach { opt ->
                val vehicleInfo = opt.vehicleModelVehicleBrandName + " " + opt.vehicleModelName + " " + opt.licencePlate
                DropdownMenuItem(
                    text = { Text(vehicleInfo, color = White) },
                    onClick = {
                        expanded = false
                        onOptionSelected(opt.id)
                        onSelectedTextChanged(selectedText)
                    },
                    modifier = Modifier.background(Color(36, 45, 64))
                )
            }
        }
    }
}