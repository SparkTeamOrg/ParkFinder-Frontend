package com.app.parkfinder.ui.activities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.app.parkfinder.logic.AppPreferences
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.dtos.UserDto
import com.app.parkfinder.logic.services.ImageService
import com.app.parkfinder.logic.services.TokenService
import com.app.parkfinder.ui.screens.auth.NavigationScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.utilis.ImageUtils
import com.auth0.android.jwt.JWT
import com.canhub.cropper.CropImageContract
import okhttp3.MultipartBody
import org.osmdroid.config.Configuration

class NavigationActivity : BaseActivity() {
    private val imageService = RetrofitConfig.createService(ImageService::class.java)
    private var currentImageUrl by mutableStateOf<Uri?>(null)
    private var user: UserDto = UserDto()

    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) ImageUtils.openCropper(uri, cropImage)
    }
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) result.uriContent?.let { uploadImage(user.Id, it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        user = decodeJwt()
        lifecycleScope.launch {
            val imageUriString = getProfileImageUrl(user.Id)
            currentImageUrl = if (imageUriString != null) Uri.parse(imageUriString) else null
        }

        setContent {
            ParkFinderTheme {
                NavigationScreen(
                    logout = { logout() },
                    user = user,
                    currentImageUrl = currentImageUrl,
                    openImagePicker = { ImageUtils.openImagePicker(pickMedia) },
                    removeImage = { removeImage() }
                )
            }
        }
    }

    private fun logout() {
        val tokenService = RetrofitConfig.createService(TokenService::class.java)
        lifecycleScope.launch {
            val deleteResponse = tokenService.delete(user.Id)
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

    private suspend fun getProfileImageUrl(userId: Int): String? {
        val response = imageService.getProfileImage(userId)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.isSuccessful) {
                body.data
            } else {
                Log.d("Error", body?.messages?.get(0) ?: "Unknown error")
                null
            }
        } else {
            null
        }
    }

    private fun uploadImage(userId: Int, imageUrl: Uri) {
        lifecycleScope.launch {
            val profileImage = ImageUtils.createMultipartFromUri(contentResolver, imageUrl)
            val userIdPart = MultipartBody.Part.createFormData("UserId", userId.toString())

            val response = if (profileImage != null) {
                imageService.uploadImage(userIdPart, profileImage)
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
            val response = imageService.removeImage(user.Id)
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
}