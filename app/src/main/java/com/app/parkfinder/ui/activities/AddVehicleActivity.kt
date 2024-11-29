package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.models.dtos.CreateVehicleDto
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.screens.auth.RegisterVehicleInfoScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validateLicencePlate
import com.auth0.android.jwt.JWT

class AddVehicleActivity : BaseActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()

    private var selectedBrand: Int = 0
    private var selectedModel: Int = 0
    private var selectedColor: Int = 0
    private var licencePlate = mutableStateOf("")

    private val colorNames = mapOf(
        1 to "Red",
        2 to "Green",
        3 to "Blue",
        4 to "Yellow",
        5 to "Cyan",
        6 to "Magenta",
        7 to "Gray",
        8 to "Black"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                RegisterVehicleInfoScreen (
                    onSelectedBrandChange = { selectedBrand = it },
                    onSelectedModelChange = { selectedModel = it },
                    onSelectedColorChange = { selectedColor = it },
                    licencePlate = licencePlate.value,
                    onLicencePlateChange = { licencePlate.value = it },
                    colorNames = colorNames,
                    onBackClick = { navigateToVehicleInfo() },
                    register = { registerVehicle() }
                )
            }

            vehicleViewModel.registerVehicleResult.observe(this) { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this, "Vehicle added successfully", Toast.LENGTH_LONG).show()
                    navigateToVehicleInfo()
                }
                else {
                    Toast.makeText(this, result.data, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun registerVehicle(): List<Boolean> {
        val brandError = selectedBrand == 0
        val modelError = selectedModel == 0
        val colorError = selectedColor == 0
        val regNumError = !validateLicencePlate(licencePlate.value)

        if (brandError || modelError || colorError || regNumError) {
            return listOf(brandError, modelError, colorError, regNumError)
        }

        val vehicle = CreateVehicleDto(
            licencePlate = licencePlate.value,
            color = colorNames[selectedColor] ?: "",
            userId = getUserId(),
            modelId = selectedModel
        )

        vehicleViewModel.registerVehicle(vehicle)
        return List(4){false}
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

    private fun navigateToVehicleInfo() {
        val intent = Intent(this, VehicleInfoActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
        finish()
    }
}