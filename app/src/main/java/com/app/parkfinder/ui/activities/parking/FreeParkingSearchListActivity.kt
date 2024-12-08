package com.app.parkfinder.ui.activities.parking

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingListScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class FreeParkingSearchListActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()

    private var parkings = mutableListOf<ParkingLotDto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = intent.getStringExtra("location")
        val radius = intent.getIntExtra("radius", 1)
        mapViewModel.searchByLocation(location!!,radius)

        mapViewModel.getAllParkingLotsAroundLocationRes.observe(this) { result ->
            if (result != null) {
                if (result.isSuccessful) {
                    parkings = result.data.toMutableList()
                    setContent {
                        ParkFinderTheme {
                            ParkingListScreen(
                                parkingSpaces = parkings,
                                navigateToParkingSpots = {lot, name ->
                                    navigateToParkingSpotsList(lot,name)
                                }
                            )
                        }
                    }
                } else {
                    Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else {
                Toast.makeText(this, "No parking lots found", Toast.LENGTH_LONG).show()
                finish()
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
