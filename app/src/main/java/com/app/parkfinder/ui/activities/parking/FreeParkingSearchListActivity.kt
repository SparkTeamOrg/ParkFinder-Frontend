package com.app.parkfinder.ui.activities.parking

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.services.MapService
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingScreenPreview
import com.app.parkfinder.ui.theme.ParkFinderTheme
import java.util.logging.Logger

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
                            ParkingScreenPreview(parkings)
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


}
