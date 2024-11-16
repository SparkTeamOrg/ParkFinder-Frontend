package com.app.parkfinder.ui.activities

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.ui.screens.auth.RegisterUserDataScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.validatePhoneNumber
import com.app.parkfinder.utilis.validateUserName
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

class RegisterUserDataActivity : BaseActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var verificationCode: String

    private var fullName = mutableStateOf("")
    private var phoneNumber = mutableStateOf("")

    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            startCrop(uri)
        }
    }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            profileImage.value = result.uriContent
        } else {
            val exception = result.error
            println("Crop failed: ${exception?.message}")
        }
    }
    private var profileImage = mutableStateOf<Uri?>(null)


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
                    openImagePicker = { openImagePicker() },
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
            putExtra("profileImage", profileImage.toString())
        }

        val options =
            ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun openImagePicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCrop(uri: Uri) {
        val options = CropImageContractOptions(
            uri,
            CropImageOptions(
                imageSourceIncludeGallery = true,
                cropShape = CropImageView.CropShape.RECTANGLE,
                fixAspectRatio = true,
                aspectRatioX = 1,
                aspectRatioY = 1,
                canChangeCropWindow = true,
                centerMoveEnabled = true,
                initialCropWindowRectangle = Rect(100, 100, 900, 900),
                activityBackgroundColor = 0xFF151A24.toInt(),
                toolbarColor = 0xFF1B1B1B.toInt(),
                toolbarBackButtonColor = 0xFFFFFF,
                toolbarTintColor = 0xFFFFFF,
                cropMenuCropButtonTitle = "Done",
                minCropWindowWidth = 800,
                minCropWindowHeight = 800,
                maxCropResultWidth = 800,
                maxCropResultHeight = 800
            )
        )
        cropImage.launch(options)
    }
}