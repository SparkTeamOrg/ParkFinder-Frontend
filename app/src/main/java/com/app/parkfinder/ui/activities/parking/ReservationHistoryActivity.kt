package com.app.parkfinder.ui.activities.parking

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ReservationHistoryItemDto
import com.app.parkfinder.logic.models.dtos.pagination.ReservationHistoryPaginationRequest
import com.app.parkfinder.logic.view_models.ReservationHistoryViewModel
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.ReservationHistoryScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ReservationHistoryActivity : BaseActivity() {
    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val reservationHistoryViewModel: ReservationHistoryViewModel by viewModels()

    private var reservationHistory = emptyList<ReservationHistoryItemDto>()
    private var selectedStartDate = mutableStateOf<String?>(null)
    private var selectedEndDate = mutableStateOf<String?>(null)
    private var selectedVehicleId = mutableStateOf<Int?>(null)
    private var selectedSortOption = mutableStateOf("Date")
    private var selectedAscDscOption = mutableStateOf("Descending")

    private var isHistoryLoading = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vehicleViewModel.getUserVehicles()

        setContent{
            val userVehicles = vehicleViewModel.userVehiclesResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))
            val reservationHistoryResponse = reservationHistoryViewModel.paginateReservationHistoryResult.observeAsState()

            LaunchedEffect(Unit) {
                getPaginatedReservationHistory(1)
            }

            LaunchedEffect(reservationHistoryResponse.value) {
                if(reservationHistoryResponse.value?.isSuccessful == true)
                    isHistoryLoading.value = false
            }

            reservationHistory += (reservationHistoryResponse.value?.data?.items ?: emptyList())

            ParkFinderTheme {
                ReservationHistoryScreen(
                    vehicles = userVehicles.value.data,
                    onSelectedStartDateChanged = { selectedStartDate.value = it },
                    onSelectedEndDateChanged = { selectedEndDate.value = it },
                    onSelectedVehicleIdChanged = { selectedVehicleId.value = it },
                    onSelectedSortOptionChanged = { selectedSortOption.value = it },
                    onSelectedAscDscOptionChanged = { selectedAscDscOption.value = it },
                    selectedSortOption = selectedSortOption.value,
                    selectedAscDscOption = selectedAscDscOption.value,
                    onBackClick = { finish() },
                    reservationHistory = reservationHistory,
                    isHistoryLoading = isHistoryLoading.value,
                    getPaginatedReservationHistory = { currentPage -> getPaginatedReservationHistory(currentPage) }
                )
            }
        }
    }

    private fun getPaginatedReservationHistory(currentPage: Int){
        if(isHistoryLoading.value) return

        isHistoryLoading.value = true

        val request = ReservationHistoryPaginationRequest(
            page = currentPage,
            limit = 10,
            sortBy = selectedSortOption.value,
            sortDescending = selectedAscDscOption.value == "Descending",
            vehicleId = selectedVehicleId.value,
            startDate = selectedStartDate.value,
            searchTerm = "",
            endDate = selectedEndDate.value
        )

        reservationHistoryViewModel.getPaginatedReservationHistory(request)
    }
}