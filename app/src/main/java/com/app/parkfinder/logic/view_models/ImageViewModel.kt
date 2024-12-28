package com.app.parkfinder.logic.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.services.ImageService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ImageViewModel : ViewModel() {
    private val imageService = RetrofitConfig.createService(ImageService:: class.java)

    private val _getProfileImageResult = MutableLiveData<BackResponse<String>>()
    private val _uploadProfileImageResult = MutableLiveData<BackResponse<String>>()
    private val _removeProfileImageResult = MutableLiveData<BackResponse<String>>()

    val getProfileImageResult : LiveData<BackResponse<String>> = _getProfileImageResult
    val uploadProfileImageResult: LiveData<BackResponse<String>> = _uploadProfileImageResult
    val removeProfileImageResult: LiveData<BackResponse<String>> = _removeProfileImageResult

    fun getProfileImage() {
        viewModelScope.launch {
            try {
                val response = imageService.getProfileImage()

                if(response.isSuccessful) {
                    response.body()?.let {
                        _getProfileImageResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    )
                    _getProfileImageResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                )
                _getProfileImageResult.postValue(errorResponse)
            }
        }
    }

    fun uploadProfileImage(profileImage: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = imageService.uploadImage(profileImage)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _uploadProfileImageResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    )
                    _uploadProfileImageResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                )
                _uploadProfileImageResult.postValue(errorResponse)
            }
        }
    }

    fun removeProfileImage() {
        viewModelScope.launch {
            try {
                val response = imageService.removeImage()

                if(response.isSuccessful) {
                    response.body()?.let {
                        _removeProfileImageResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    )
                    _removeProfileImageResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                )
                _removeProfileImageResult.postValue(errorResponse)
            }
        }
    }
}