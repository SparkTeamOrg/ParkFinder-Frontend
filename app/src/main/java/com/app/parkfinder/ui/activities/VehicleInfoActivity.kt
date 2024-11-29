package com.app.parkfinder.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.screens.main.VehicleInfoScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.auth0.android.jwt.JWT

class VehicleInfoActivity : BaseActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                VehicleInfoScreen(
                    vehicles = emptyList(),
                )
            }
        }
        loadUserVehicles()
    }

    private fun loadUserVehicles() {
        vehicleViewModel.getUserVehicles(getUserId())
    }

    override fun onResume() {
        super.onResume()
        vehicleViewModel.userVehiclesResult.observe(this) { result ->
            if (result.isSuccessful) {
                val userVehicles = result.data
                setContent {
                    ParkFinderTheme {
                        VehicleInfoScreen(
                            vehicles = userVehicles
                        )
                    }
                }
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getUserId(): Int {
        val token = AppPreferences.accessToken
        try {
            val jwt = JWT(token!!)
            return jwt.getClaim("UserId").asInt()!!
        } catch (e: Exception) {
            e.message?.let { Log.d("Debug", it) }
            e.printStackTrace()
            return -1
        }
    }
}