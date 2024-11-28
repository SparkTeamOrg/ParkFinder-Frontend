package com.app.parkfinder.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.screens.main.VehicleInfoScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import kotlinx.coroutines.launch

class VehicleInfoActivity : BaseActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUserVehicles()
    }

    private fun loadUserVehicles() {
        lifecycleScope.launch {
            vehicleViewModel.getUserVehicles(12)
        }

        vehicleViewModel.userVehiclesResult.observe(this) { result ->
            if (result.isSuccessful) {
                val userVehicles = result.data
                setContent {
                    ParkFinderTheme {
                        VehicleInfoScreen(vehicles = userVehicles)
                    }
                }
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}