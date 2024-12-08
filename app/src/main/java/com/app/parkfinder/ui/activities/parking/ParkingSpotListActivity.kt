package com.app.parkfinder.ui.activities.parking

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.logic.enums.ParkingSpotStatusEnum
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingSpotListScreen

class ParkingSpotListActivity: ComponentActivity() {
    private val mapViewModel: MapViewModel by viewModels()
    private val parkingSpots = mutableStateOf<List<ParkingSpotDto>>(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lotId = intent.getIntExtra("parkingLotId", 0)
        var parkingName = ""
        parkingName+= intent.getStringExtra("parkingName")
        mapViewModel.getParkingLotSpotsSearch(lotId)
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
            ParkingSpotListScreen(parkingSpots.value,parkingName)
        }
    }
}