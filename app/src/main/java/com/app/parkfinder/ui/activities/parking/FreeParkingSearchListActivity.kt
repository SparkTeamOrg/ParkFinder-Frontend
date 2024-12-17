package com.app.parkfinder.ui.activities.parking

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingListScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class FreeParkingSearchListActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()
    private val isLoading = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = intent.getStringExtra("location")
        val radius = intent.getIntExtra("radius", 1)

        isLoading.value = true
        mapViewModel.searchByLocation(location!!,radius)

        setContent {
            val foundParkingResult = mapViewModel.getAllParkingLotsAroundLocationRes.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))

            val parkingSpaces: List<ParkingLotDto>
            if(foundParkingResult.value != null && foundParkingResult.value!!.isSuccessful){
                isLoading.value = false
                parkingSpaces = foundParkingResult.value!!.data
            } else {
                parkingSpaces = emptyList()
            }

            ParkFinderTheme {
                ParkingListScreen(
                    parkingSpaces = parkingSpaces,
                    navigateToParkingSpots = {lot, name ->
                        navigateToParkingSpotsList(lot,name)
                    },
                    isLoading = isLoading.value
                )
            }
        }
    }

    private fun navigateToParkingSpotsList(parkingLot: ParkingLotDto, parkingName:String)
    {
        val intent = Intent(this,ParkingSpotListActivity::class.java).apply {
            putExtra("parkingLot",parkingLot)
            putExtra("parkingName",parkingName)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }


}
