package com.app.parkfinder.ui.activities.vehicle

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.VehicleDto
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.VehicleInfoScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class VehicleInfoActivity : BaseActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUserVehicles()

        setContent {
            ParkFinderTheme {
                val userVehicles = vehicleViewModel.userVehiclesResult.observeAsState(BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList()))

                VehicleInfoScreen(
                    vehicles = userVehicles.value.data,
                    onBackClick = { finish () },
                    onPlusClick = { navigateToAddVehicle() },
                    onCanClick = { vehicleId -> deleteVehicle(vehicleId)},
                    onPenClick = { dto, image -> navigateToUpdateVehicle(dto, image) }
                )
            }

            vehicleViewModel.deleteVehicleResult.observe(this) { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this, "Vehicle deleted successfully", Toast.LENGTH_LONG).show()
                    loadUserVehicles()
                }
                else {
                    Toast.makeText(this, result.messages[0], Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadUserVehicles() {
        vehicleViewModel.getUserVehicles()
    }

    private fun deleteVehicle(vehicleId: Int){
        vehicleViewModel.deleteVehicle(vehicleId)
    }

    private fun navigateToAddVehicle() {
        val intent = Intent(this, AddVehicleActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun navigateToUpdateVehicle(dto: VehicleDto, image: Int) {
        Log.d("Debug", dto.toString())
        val intent = Intent(this, UpdateVehicleActivity::class.java).apply {
            putExtra("vehicleDto", dto)
            putExtra("image", image)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
        finish()
    }
}