package com.app.parkfinder.ui.activities.parking

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.logic.view_models.MapViewModel
import com.app.parkfinder.ui.screens.parking.ParkingScreenPreview
import com.app.parkfinder.ui.theme.ParkFinderTheme

class FreeParkingSearchListActivity : ComponentActivity() {

    private val mapViewModel: MapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapViewModel.getAllParkingLotsAroundLocationRes.observe(this) { result ->
            if (result != null) {
                if (result.isSuccessful) {
                    Toast.makeText(this, "Verification code sent", Toast.LENGTH_LONG).show()
                    setContent {
                        ParkFinderTheme {
                            ParkingScreenPreview(result.data)
                        }
                    }
                } else {
                    Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else {
                Toast.makeText(this, "No parking lots found", Toast.LENGTH_LONG).show()
                finish()//return to the previous
            }
        }
    }


}
