package com.app.parkfinder.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.parkfinder.R
import com.app.parkfinder.foreground.Actions
import com.app.parkfinder.foreground.NotificationService
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.CreateReservationHistoryDto
import com.app.parkfinder.logic.models.dtos.ParkingLotDto
import com.app.parkfinder.logic.models.dtos.ParkingSpotDto
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.logic.services.ImageService
import com.app.parkfinder.logic.services.TokenService
import com.app.parkfinder.logic.view_models.ReservationHistoryViewModel
import com.app.parkfinder.logic.view_models.ReservationViewModel
import com.app.parkfinder.ui.activities.parking.FreeParkingSearchListActivity
import com.app.parkfinder.ui.activities.parking.ReservationActivity
import com.app.parkfinder.ui.activities.statistic.StatisticsActivity
import com.app.parkfinder.ui.activities.vehicle.VehicleInfoActivity
import com.app.parkfinder.ui.screens.auth.NavigationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.ImageUtils
import com.auth0.android.jwt.JWT
import com.canhub.cropper.CropImageContract
import org.osmdroid.config.Configuration

class NavigationActivity : BaseActivity() {
    private val reservationViewModel: ReservationViewModel by viewModels()
    private val reservationHistoryViewModel: ReservationHistoryViewModel by viewModels()

    private val imageService = RetrofitConfig.createService(ImageService::class.java)
    private var currentImageUrl by mutableStateOf<Uri?>(null)
    private var user: UserDto = UserDto()

    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) ImageUtils.openCropper(uri, cropImage)
    }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) result.uriContent?.let { uploadImage(it) }
    }

    @SuppressLint("NewApi")
    private fun startFpmNotificationService()
    {
        Intent(applicationContext,NotificationService::class.java).also {
            it.action = Actions.START.toString()
            startService(it)
        }
    }

    private fun stopFpmNotificationService()
    {
        val stopIntent = Intent(this, NotificationService::class.java)
        stopIntent.action = Actions.STOP.toString()
        startService(stopIntent)
    }

    @SuppressLint("InlinedApi")
    fun checkAndRequestPermissions(activity: Activity): Boolean {
        val requiredPermissions = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC
        )

        val permissionsNeeded = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsNeeded.toTypedArray(), 101)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions(this)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { /* This disables any action on back button click */}
        })

        Configuration.getInstance().userAgentValue = packageName

        user = decodeJwt()
        lifecycleScope.launch {
            val imageUriString = getProfileImageUrl()
            currentImageUrl = if (imageUriString != null) Uri.parse(imageUriString) else null
        }

        reservationViewModel.getConfirmedReservation()

        setContent {
            val confirmedReservations by reservationViewModel.getConfirmedReservationResult.observeAsState(
                BackResponse(isSuccessful = false, messages = emptyList(), data = emptyList())
            )

            ParkFinderTheme {
                NavigationScreen(
                    startFpmNotificationService = { startFpmNotificationService()},
                    stopFpmNotificationService = {stopFpmNotificationService()},
                    logout = { logout() },
                    user = user,
                    currentImageUrl = currentImageUrl,
                    openImagePicker = { ImageUtils.openImagePicker(pickMedia) },
                    removeImage = { removeImage() },
                    searchFreeParkingsAroundLocation = { loc, rad ->
                        navigateToParkingList(loc,rad)
                    },
                    confirmReservation = { id -> confirmReservation(id) },
                    cancelReservation = { id -> cancelReservation(id) },
                    addReservationHistory = { vehicleId, rating, comment -> addReservationHistory(vehicleId, rating, comment) },
                    navigateToVehicleInfo = { navigateToVehicleInfo() },
                    navigateToReservation = { spot, lot, num -> navigateToReservation(spot, lot, num) },
                    navigateToHelpCenter = { navigateToHelpCenter() },
                    reservationViewModel = reservationViewModel,
                    navigateToStatistics = {navigateToStatistics()}
                )
            }
        }

        reservationViewModel.confirmReservationResult.observe(this) { result ->
            if (result.isSuccessful) {
                Toast.makeText(this, "Reservation confirmed", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, result.messages[0], Toast.LENGTH_LONG).show()
            }
        }

        reservationViewModel.deleteReservationResult.observe(this) { result ->
            if (result.isSuccessful) {
                Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, result.messages[0], Toast.LENGTH_LONG).show()
            }
        }

        reservationHistoryViewModel.createReservationHistoryResult.observe(this) { result ->
            if (result.item1.isSuccessful) {
                reservationViewModel.getConfirmedReservation()
                Toast.makeText(this, "Reservation completed", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, result.item1.messages.joinToString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun logout() {
        val tokenService = RetrofitConfig.createService(TokenService::class.java)
        lifecycleScope.launch {
            val deleteResponse = tokenService.delete()
            if(deleteResponse.isSuccessful) {
                val body = deleteResponse.body()
                if(body != null){
                    if(body.isSuccessful){
                        clearTokens()
                    }
                }else {
                    Log.d("Error", "Error while deleting refresh token")
                }
            }
        }
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearTokens() {
        AppPreferences.removeTokens()
    }

    private fun decodeJwt(): UserDto {
        val token = AppPreferences.accessToken
        val dto = UserDto()
        try {
            val jwt = JWT(token!!)
            dto.Id = jwt.getClaim("UserId").asInt()!!
            dto.Fullname = jwt.getClaim("Fullname").asString()!!
            dto.Email = jwt.getClaim("Email").asString()!!
        } catch (e: Exception) {
            e.message?.let { Log.d("Debug", it) }
            e.printStackTrace()
        }
        return dto
    }

    private suspend fun getProfileImageUrl(): String? {
        val response = imageService.getProfileImage()
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.isSuccessful) {
                body.data
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun uploadImage(imageUrl: Uri) {
        lifecycleScope.launch {
            val profileImage = ImageUtils.createMultipartFromUri(contentResolver, imageUrl)

            val response = if (profileImage != null) {
                imageService.uploadImage(profileImage)
            } else null

            if (response != null && response.isSuccessful) {
                val body = response.body()
                if (body != null && body.isSuccessful) {
                    currentImageUrl = Uri.parse(body.data)
                } else {
                    Log.d("Error", body?.messages?.get(0) ?: "Unknown error")
                }
            }
        }
    }

    private fun removeImage() {
        lifecycleScope.launch {
            val response = imageService.removeImage()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.isSuccessful) {
                    currentImageUrl = null
                } else {
                    Log.d("Error", body?.messages?.get(0) ?: "Unknown error")
                }
            }
        }
    }

    private fun confirmReservation(id: Int){
        reservationViewModel.confirmReservation(id)
    }

    private fun cancelReservation(id: Int){
        reservationViewModel.deleteReservation(id)
    }

    private fun addReservationHistory(vehicleId: Int, rating: Int, comment: String){
        var com : String? = comment
        if(comment.isEmpty()){
            com = null
        }
        val createReservationHistoryDto = CreateReservationHistoryDto(
            vehicleId = vehicleId,
            rating = rating,
            comment = com
        )
        reservationHistoryViewModel.addReservationHistory(createReservationHistoryDto)
    }

    private fun navigateToHelpCenter() {
        val intent = Intent(this, HelpCenterActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToVehicleInfo() {
        val intent = Intent(this, VehicleInfoActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToParkingList(location: String, radius: Int):Unit {
        val intent = Intent(this, FreeParkingSearchListActivity::class.java).apply {
            putExtra("location",location)
            putExtra("radius",radius)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToStatistics()
    {
        val intent = Intent(this, StatisticsActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }

    private fun navigateToReservation(spot: ParkingSpotDto, lot: ParkingLotDto, spotNumber: String) {
        val intent = Intent(this, ReservationActivity::class.java).apply {
            putExtra("parking_spot", spot)
            putExtra("parking_lot", lot)
            putExtra("spot_number", spotNumber)
        }
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}