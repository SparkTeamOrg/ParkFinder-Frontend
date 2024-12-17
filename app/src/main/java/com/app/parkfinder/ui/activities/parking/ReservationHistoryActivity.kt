package com.app.parkfinder.ui.activities.parking

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.view_models.ReservationHistoryViewModel
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.ReservationHistoryScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ReservationHistoryActivity : BaseActivity() {
    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val reservationHistoryViewModel: ReservationHistoryViewModel by viewModels()

    private var selectedStartDate = mutableStateOf<String?>(null)
    private var selectedEndDate = mutableStateOf<String?>(null)
    private var selectedVehicleId = mutableStateOf<Int?>(null)
    private var selectedSortOption = mutableStateOf("Date")
    private var selectedAscDscOption = mutableStateOf("Descending")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vehicleViewModel.getUserVehicles()

        setContent{
            val userVehicles = vehicleViewModel.userVehiclesResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))
            val reservationHistories = reservationHistoryViewModel.reservationHistories.collectAsLazyPagingItems()

            ParkFinderTheme {
                ReservationHistoryScreen(
                    vehicles = userVehicles.value.data,
                    onSelectedStartDateChanged = { selectedStartDate.value = it },
                    onSelectedEndDateChanged = { selectedEndDate.value = it },
                    onSelectedVehicleIdChanged = { selectedVehicleId.value = it },
                    onSelectedSortOptionChanged = { selectedSortOption.value = it },
                    onSelectedAscDscOptionChanged = { selectedAscDscOption.value = it },
                    selectedVehicleId = selectedVehicleId.value,
                    selectedStartDate = selectedStartDate.value,
                    selectedEndDate = selectedEndDate.value,
                    selectedSortOption = selectedSortOption.value,
                    selectedAscDscOption = selectedAscDscOption.value,
                    onBackClick = { finish() },
                    reservationHistories = reservationHistories,
                    applyFilters = { applyFilters() },
                    resetFilters = { removeFilters() }
                )
            }
        }
    }

    private fun applyFilters(){
        reservationHistoryViewModel.setFilter(
            sortBy = selectedSortOption.value,
            sortDescending = selectedAscDscOption.value == "Descending",
            vehicleId = selectedVehicleId.value,
            startDate = selectedStartDate.value,
            endDate = selectedEndDate.value
        )
        reservationHistoryViewModel.refreshReservationHistory()
    }

    private fun removeFilters(){
        selectedVehicleId.value = null
        selectedStartDate.value = null
        selectedStartDate.value = null
        selectedAscDscOption.value = "Descending"
        selectedSortOption.value = "Date"
    }
}