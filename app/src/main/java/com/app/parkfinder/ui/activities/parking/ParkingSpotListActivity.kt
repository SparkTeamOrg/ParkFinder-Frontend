package com.app.parkfinder.ui.activities.parking

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingSpotListScreen

class ParkingSpotListActivity: ComponentActivity() {
    private val mapViewModel: MapViewModel by viewModels()
    private val parkingSpots = mutableStateOf<List<ParkingSpotDto>>(emptyList())
    private lateinit var parkingLot: ParkingLotDto
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parkingLot = intent.getParcelableExtra("parkingLot",ParkingLotDto::class.java)!!
        var parkingName = ""
        parkingName+= intent.getStringExtra("parkingName")
        mapViewModel.getParkingLotSpotsSearch(parkingLot.id)
        mapViewModel.getParkingSpotUpdate.observe(this){result ->
            val updatedSpots = parkingSpots.value.toMutableList() // Create a new list
            for (update in result) {
                val index = updatedSpots.indexOfFirst { it.id == update.parkingSpotId }
                if (index != -1) {
                    val updatedSpot = updatedSpots[index].copy(parkingSpotStatus = update.parkingSpotStatus)
                    updatedSpots[index] = updatedSpot
                }
            }
            parkingSpots.value = updatedSpots // Update the state with the new list
        }
        mapViewModel.getParkingSpotsForParkingLotSearch.observe(this){spots->
            if(spots.isSuccessful)
            {
                Log.d("Parking","Spots received ${spots.data.size}")
                parkingSpots.value = spots.data.toMutableList()
            }
        }
        setContent {
            ParkingSpotListScreen(
                parkingSpaces = parkingSpots.value,
                parkingLotName = parkingName,
                navigateToReservation = { spot, num -> navigateToReservation(spot, num) }
            )
        }
    }

    private fun navigateToReservation(spot: ParkingSpotDto, spotNumber: String) {
        val intent = Intent(this, ReservationActivity::class.java).apply {
            putExtra("parking_spot", spot)
            putExtra("parking_lot", parkingLot)
            putExtra("spot_number", spotNumber)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}