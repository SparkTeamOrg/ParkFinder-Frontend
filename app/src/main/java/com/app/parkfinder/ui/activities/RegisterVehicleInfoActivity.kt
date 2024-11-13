package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.UserRegisterDto
import com.app.parkfinder.logic.view_models.AuthViewModel
import com.app.parkfinder.ui.screens.auth.RegisterUserDataScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validateLicencePlate

class RegisterVehicleInfoActivity: BaseActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var verificationCode: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phoneNumber: String
    private lateinit var profileImage: String

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

        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        verificationCode = intent.getStringExtra("code") ?: ""
        firstName = intent.getStringExtra("firstName") ?: ""
        lastName = intent.getStringExtra("lastName") ?: ""
        phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        profileImage = intent.getStringExtra("profileImage") ?: ""

        super.onCreate(savedInstanceState)
        setContent {
            ParkFinderTheme {
                RegisterUserDataScreen (
                    onSelectedBrandChange = { selectedBrand = it },
                    onSelectedModelChange = { selectedModel = it },
                    onSelectedColorChange = { selectedColor = it },
                    licencePlate = licencePlate.value,
                    onLicencePlateChange = { licencePlate.value = it },
                    colorNames = colorNames,
                    onBackClick = { finish() },
                    register = { registerUser() }
                )
            }
        }

        authViewModel.registrationResult.observe(this) { result ->
            if (result.isSuccessful) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show()
                navigateToLogin()
            }
            else {
                Toast.makeText(this, result.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerUser() : List<Boolean> {

        val brandError = selectedBrand == 0
        val modelError = selectedModel == 0
        val colorError = selectedColor == 0
        val regNumError = !validateLicencePlate(licencePlate.value)

        if (brandError || modelError || colorError || regNumError) {
            return listOf(brandError, modelError, colorError, regNumError)
        }

        val userModel = UserRegisterDto(
            email = email,
            password = password,
            verificationCode = verificationCode,
            firstName = firstName,
            lastName = lastName,
            mobilePhone = phoneNumber,
            profileImage = profileImage,
            modelId = selectedModel,
            color = colorNames[selectedColor] ?: "",
            licencePlate = licencePlate.value
        )

        authViewModel.register(userModel)
        return List(4){false}
    }

    private fun navigateToLogin() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}