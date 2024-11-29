package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.R
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
                    onBackClick = { finish () },
                    onPlusClick = { navigateToAddVehicle() }
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
                            vehicles = userVehicles,
                            onBackClick = { finish() },
                            onPlusClick = { navigateToAddVehicle() }
                        )
                    }
                }
            } else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun navigateToAddVehicle() {
        val intent = Intent(this, AddVehicleActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
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