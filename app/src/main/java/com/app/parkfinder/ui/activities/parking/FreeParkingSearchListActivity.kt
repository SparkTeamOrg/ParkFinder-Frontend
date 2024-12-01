package com.app.parkfinder.ui.activities.parking

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.services.MapService
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingScreenPreview
import com.app.parkfinder.ui.theme.ParkFinderTheme
import java.util.logging.Logger

class FreeParkingSearchListActivity : ComponentActivity() {

    private val mapService = RetrofitConfig.createService(MapService::class.java)

    private val mapViewModel: MapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapViewModel.getAllParkingLotsAroundLocationRes.observe(this) { result ->
            Logger.getLogger("FreeParkingSearchListActivity").info("Find parking lots around location")
            if (result != null) {
                Logger.getLogger("FreeParkingSearchListActivity").info("Result is not null")
                if (result.isSuccessful) {
                    Logger.getLogger("FreeParkingSearchListActivity").info("Parking lots found")
                    Logger.getLogger("FreeParkingSearchListActivity").info(result.data.toString())
                    setContent {
                        ParkFinderTheme {
                            ParkingScreenPreview(result.data)
                        }
                    }
                } else {
                    Logger.getLogger("FreeParkingSearchListActivity").info("No parking lots found")
                    Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
//                    finish()
                }
            }
            else {
                Logger.getLogger("FreeParkingSearchListActivity").info("No parking lots found")
                Toast.makeText(this, "No parking lots found", Toast.LENGTH_LONG).show()
//                finish()
            }
        }
    }


}
