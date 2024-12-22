package com.app.parkfinder.ui.activities.vehicle

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.CreateVehicleDto
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.auth.register.RegisterVehicleInfoScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.ColorUtilis
import com.app.parkfinder.utilis.validateLicencePlate

class AddVehicleActivity : BaseActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()

    private var selectedBrand: Int = 0
    private var selectedModel: Int = 0
    private var selectedColor: Int = 0
    private var licencePlate = mutableStateOf("")

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
                    colorNames = ColorUtilis.getColorNames(),
                    onBackClick = { navigateToVehicleInfo() },
                    register = { addVehicle() }
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

    private fun addVehicle(): List<Boolean> {
        val brandError = selectedBrand == 0
        val modelError = selectedModel == 0
        val colorError = selectedColor == 0
        val regNumError = !validateLicencePlate(licencePlate.value)

        if (brandError || modelError || colorError || regNumError) {
            return listOf(brandError, modelError, colorError, regNumError)
        }

        val vehicle = CreateVehicleDto(
            licencePlate = licencePlate.value,
            color = ColorUtilis.getColorName(selectedColor),
            modelId = selectedModel
        )

        vehicleViewModel.registerVehicle(vehicle)
        return List(4){false}
    }

    private fun navigateToVehicleInfo() {
        val intent = Intent(this, VehicleInfoActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
        finish()
    }
}