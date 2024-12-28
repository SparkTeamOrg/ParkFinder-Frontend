package com.app.parkfinder.logic.view_models

import android.media.session.MediaSession.Token
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.TokenDto
import com.app.parkfinder.logic.services.TokenService
import kotlinx.coroutines.launch

class TokenViewModel : ViewModel() {
    private val tokenService = RetrofitConfig.createService(TokenService:: class.java)

    private val _deleteTokensResult = MutableLiveData<BackResponse<String>>()
    private val _refreshTokensResult = MutableLiveData<BackResponse<TokenDto>>()

    val deleteTokensResult: LiveData<BackResponse<String>> = _deleteTokensResult
    val refreshTokensResult: LiveData<BackResponse<TokenDto>> = _refreshTokensResult

    fun deleteTokens() {
        viewModelScope.launch {
            try {
                val response = tokenService.delete()

                if(response.isSuccessful) {
                    response.body()?.let {
                        _deleteTokensResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    )
                    _deleteTokensResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                )
                _deleteTokensResult.postValue(errorResponse)
            }
        }
    }

    fun refreshTokens(tokens: TokenDto) {
        viewModelScope.launch {
            try {
                val response = tokenService.refresh(tokens)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _refreshTokensResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = TokenDto(null, null)
                    )
                    _refreshTokensResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = TokenDto(null, null)
                )
                _refreshTokensResult.postValue(errorResponse)
            }
        }
    }
}