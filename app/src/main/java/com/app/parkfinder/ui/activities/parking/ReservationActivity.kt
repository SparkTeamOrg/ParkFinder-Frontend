package com.app.parkfinder.ui.activities.parking

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.app.parkfinder.logic.NavigationStatus
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationDto
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.view_models.ReservationViewModel
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.ReservationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ReservationActivity : BaseActivity() {
    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val reservationViewModel: ReservationViewModel by viewModels()

    private var selectedVehicle by mutableIntStateOf(0)
    private var spotId: Int? = -1

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vehicleViewModel.getUserVehicles()

        setContent {
            spotId = intent.getIntExtra("parking_spot_id", 0)
            val lot = intent.getParcelableExtra("parking_lot", ParkingLotDto::class.java)
            val spotNumber = intent.getStringExtra("spot_number")

            ParkFinderTheme {
                val userVehicles = vehicleViewModel.userVehiclesResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))

                if (lot != null && spotNumber != null) {
                    ReservationScreen(
                        lot = lot,
                        spotNumber = spotNumber,
                        startNavigation = { startNavigation() },
                        vehicles = userVehicles.value.data,
                        selectedVehicle = selectedVehicle,
                        onSelectedVehicleChange = { selectedVehicle = it }
                    )
                }

                reservationViewModel.createReservationResult.observe(this) { result ->
                    if (result.isSuccessful) {
                        Toast.makeText(this, "Reservation added successfully", Toast.LENGTH_LONG).show()
                        NavigationStatus.signalParkingSpotReserved()
                        finish()
                    }
                    else {
                        Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun startNavigation(){
        val createReservationDto = spotId?.let {
            CreateReservationDto(
                parkingSpotId = it,
                vehicleId = selectedVehicle
            )
        }

        if (createReservationDto != null) {
            reservationViewModel.createReservation(createReservationDto)
        }
    }
}