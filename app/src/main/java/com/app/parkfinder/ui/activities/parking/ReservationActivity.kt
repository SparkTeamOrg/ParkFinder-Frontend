package com.app.parkfinder.ui.activities.parking

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.view_models.ReservationHistoryViewModel
import com.app.parkfinder.logic.view_models.ReservationViewModel
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.ReservationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ReservationActivity : BaseActivity() {
    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val reservationViewModel: ReservationViewModel by viewModels()
    private val reservationHistoryViewModel: ReservationHistoryViewModel by viewModels()

    private var selectedVehicle by mutableIntStateOf(0)
    private var spot: ParkingSpotDto? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            spot = intent.getParcelableExtra("parking_spot", ParkingSpotDto::class.java)
            val lot = intent.getParcelableExtra("parking_lot", ParkingLotDto::class.java)
            val spotNumber = intent.getStringExtra("spot_number")

            vehicleViewModel.getUserVehicles()
            spot?.let { reservationHistoryViewModel.getParkingSpotComments(it.id) }
            spot?.let { reservationHistoryViewModel.getParkingSpotRating(it.id) }

            ParkFinderTheme {
                val userVehicles = vehicleViewModel.userVehiclesResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))
                val comments = reservationHistoryViewModel.parkingSpotCommentsResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))
                val rating = reservationHistoryViewModel.parkingSpotRatingResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = -1.0))

                Log.d("Debug", spot?.id.toString())
                Log.d("Debug", comments.toString())
                Log.d("Debug", rating.toString())

                if (lot != null && spotNumber != null) {
                    ReservationScreen(
                        lot = lot,
                        spot = spot!!,
                        spotNumber = spotNumber,
                        comments = comments.value.data,
                        rating = rating.value.data,
                        startNavigation = { startNavigation() },
                        vehicles = userVehicles.value.data,
                        selectedVehicle = selectedVehicle,
                        onSelectedVehicleChange = { selectedVehicle = it }
                    )
                }
            }

            reservationViewModel.createReservationResult.observe(this) { result ->
                if (result.isSuccessful) {
                    Log.d("Debug", result.data.toString())
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

    private fun startNavigation(){
        val createReservationDto = spot?.id?.let {
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