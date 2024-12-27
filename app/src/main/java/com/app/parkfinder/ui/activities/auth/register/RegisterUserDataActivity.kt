package com.app.parkfinder.ui.activities.auth.register

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.auth.register.RegisterUserDataScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.ImageUtils
import com.app.parkfinder.utilis.validatePhoneNumber
import com.app.parkfinder.utilis.validateUserName
import com.canhub.cropper.CropImageContract

class RegisterUserDataActivity : ComponentActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var verificationCode: String

    private var fullName = mutableStateOf("")
    private var phoneNumber = mutableStateOf("")
    private var profileImage = mutableStateOf<Uri?>(null)

    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) ImageUtils.openCropper(uri, cropImage)
    }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) profileImage.value = result.uriContent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        verificationCode = intent.getStringExtra("code") ?: ""

        setContent {
            ParkFinderTheme {
                RegisterUserDataScreen(
                    fullName = fullName.value,
                    onFullNameChange = { fullName.value = it },
                    phoneNumber = phoneNumber.value,
                    onPhoneNumberChange = { phoneNumber.value = it },
                    onBackClick = { finish() },
                    onNextClick = { navigateToVehicleInfoEntry() },
                    validateUserName = { validateUserName(fullName.value) },
                    validatePhoneNumber = { validatePhoneNumber(phoneNumber.value) },
                    openImagePicker = { ImageUtils.openImagePicker(pickMedia) },
                    profileImage = profileImage.value,
                    onProfileImageChange = { profileImage.value = it }
                )
            }
        }
    }

    private fun navigateToVehicleInfoEntry() {
        val intent = Intent(this, RegisterVehicleInfoActivity::class.java).apply {
            putExtra("email", email)
            putExtra("password", password)
            putExtra("code", verificationCode)
            putExtra("firstName", fullName.value.split(" ")[0])
            putExtra("lastName", fullName.value.split(" ")[1])
            putExtra("phoneNumber", phoneNumber.value)
            putExtra("profileImage", profileImage.value.toString())
        }

        val options =
            ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}