package com.app.parkfinder.ui.activities.vehicle

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.UpdateVehicleDto
import com.app.parkfinder.logic.models.dtos.VehicleDto
import com.app.parkfinder.logic.view_models.VehicleViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.auth.register.RegisterVehicleInfoScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validateLicencePlate

class UpdateVehicleActivity : BaseActivity() {
    private val vehicleViewModel: VehicleViewModel by viewModels()

    private var vehicleDto: VehicleDto? = null
    private var vehicleId by mutableIntStateOf(0)
    private var selectedBrand by mutableIntStateOf(0)
    private var selectedBrandName = mutableStateOf<String?>(null)
    private var selectedModel by mutableIntStateOf(0)
    private var selectedModelName = mutableStateOf<String?>(null)
    private var selectedColor by mutableIntStateOf(0)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            vehicleDto = intent.getParcelableExtra("vehicleDto", VehicleDto::class.java)
            val image: Int = intent.getIntExtra("image", R.drawable.car1)
            vehicleId = vehicleDto?.id ?: -1
            selectedBrand = vehicleDto?.vehicleModelVehicleBrandId ?: -1
            selectedBrandName.value = vehicleDto?.vehicleModelVehicleBrandName
            selectedModel = vehicleDto?.vehicleModelId ?: -1
            selectedModelName.value = vehicleDto?.vehicleModelName
            licencePlate.value = vehicleDto?.licencePlate ?: ""
            selectedColor = getColorId(vehicleDto?.color ?: "")

            ParkFinderTheme {
                RegisterVehicleInfoScreen(
                    selectedBrand = selectedBrand,
                    selectedBrandName = selectedBrandName.value,
                    selectedModelName = selectedModelName.value,
                    selectedColor = selectedColor,
                    onSelectedBrandChange = { selectedBrand = it },
                    onSelectedModelChange = { selectedModel = it },
                    onSelectedColorChange = { selectedColor = it },
                    licencePlate = licencePlate.value,
                    onLicencePlateChange = { licencePlate.value = it },
                    colorNames = colorNames,
                    onBackClick = { navigateToVehicleInfo() },
                    checkIfModified = { checkIfModified() },
                    image = image,
                    register = { updateVehicle() }
                )
            }

            vehicleViewModel.updateVehicleResult.observe(this) { result ->
                if (result.isSuccessful) {
                    Toast.makeText(this, "Vehicle updated successfully", Toast.LENGTH_LONG).show()
                    navigateToVehicleInfo()
                }
                else {
                    Toast.makeText(this, result.messages[0], Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateVehicle(): List<Boolean> {
        val brandError = selectedBrand == 0
        val modelError = selectedModel == 0
        val colorError = selectedColor == 0
        val regNumError = !validateLicencePlate(licencePlate.value)

        if (brandError || modelError || colorError || regNumError) {
            return listOf(brandError, modelError, colorError, regNumError)
        }

        val updateDto = UpdateVehicleDto(
                id = vehicleId,
                modelId = selectedModel,
                color = colorNames[selectedColor] ?: "",
                licencePlate = licencePlate.value,
        )

        vehicleViewModel.updateVehicle(updateDto)

        return List(4){false}
    }

    private fun checkIfModified(): Boolean {
        return selectedBrand != vehicleDto?.vehicleModelVehicleBrandId ||
                selectedModel != vehicleDto?.vehicleModelId ||
                selectedColor != getColorId(vehicleDto!!.color) ||
                licencePlate.value != vehicleDto!!.licencePlate
    }

    private fun getColorId(colorName: String): Int {
        return colorNames.entries.find { it.value.equals(colorName, ignoreCase = true) }?.key ?: -1
    }

    private fun navigateToVehicleInfo() {
        val intent = Intent(this, VehicleInfoActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
        finish()
    }
}