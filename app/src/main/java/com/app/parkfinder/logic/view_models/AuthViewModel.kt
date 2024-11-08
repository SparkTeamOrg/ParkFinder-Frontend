package com.app.parkfinder.logic.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.models.dtos.UserRegisterDto
import com.app.parkfinder.logic.services.AuthService
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val authService = RetrofitConfig.createService(AuthService::class.java)
    private val _loginResult = MutableLiveData<BackResponse<String>>()
    private val _sendingVerificationCodeForRegistrationResult = MutableLiveData<BackResponse<String>>()
    private val _verifyingCodeResult = MutableLiveData<BackResponse<String>>()
    private val _registrationResult = MutableLiveData<BackResponse<String>>()

    val loginResult: LiveData<BackResponse<String>> = _loginResult
    val sendingVerificationCodeForRegistrationResult: LiveData<BackResponse<String>> = _sendingVerificationCodeForRegistrationResult
    val verifyingCodeResult: LiveData<BackResponse<String>> = _verifyingCodeResult
    val registrationResult: LiveData<BackResponse<String>> = _registrationResult

    fun login(loginDto: UserLoginDto) {
        viewModelScope.launch {
            try {
                val response = authService.login(loginDto)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _loginResult.postValue(it)
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    ).let {
                        _loginResult.postValue(
                            it
                        )
                    }
                }

                // Note that all responses will be a successful response.
                // However, the response's isSuccessful parameter can be false
                // even if the response code is 200 OK
                response.body()?.let {
                    _loginResult.postValue(it)
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                ).let {
                    _loginResult.postValue(
                        it
                    )
                }
            }
        }
    }

    fun sendVerificationCodeForRegistration(email:String) {
        viewModelScope.launch {
            try {
                val response = authService.sendVerificationCodeForRegistration(email)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.isSuccessful) {
                            BackResponse(
                                isSuccessful = true,
                                messages = responseBody.messages,
                                data = ""
                            ).let {
                                _sendingVerificationCodeForRegistrationResult.postValue(
                                    it
                                )
                            }
                        }
                        else {
                            BackResponse(
                                isSuccessful = false,
                                messages = responseBody.messages,
                                data = ""
                            ).let {
                                _sendingVerificationCodeForRegistrationResult.postValue(
                                    it
                                )
                            }
                        }
                    }
                    else {
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf("An error occurred"),
                            data = ""
                        ).let {
                            _sendingVerificationCodeForRegistrationResult.postValue(
                                it
                            )
                        }
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    ).let {
                        _sendingVerificationCodeForRegistrationResult.postValue(
                            it
                        )
                    }
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                ).let {
                    _sendingVerificationCodeForRegistrationResult.postValue(
                        it
                    )
                }
            }
        }
    }

    fun verifyVerificationCode(email:String, verificationCode: String) {
        viewModelScope.launch {
            try {
                val response = authService.verifyVerificationCode(email, verificationCode)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.isSuccessful) {
                            BackResponse(
                                isSuccessful = true,
                                messages = responseBody.messages,
                                data = ""
                            ).let {
                                _verifyingCodeResult.postValue(
                                    it
                                )
                            }
                        }
                        else {
                            BackResponse(
                                isSuccessful = false,
                                messages = responseBody.messages,
                                data = ""
                            ).let {
                                _verifyingCodeResult.postValue(
                                    it
                                )
                            }
                        }
                    }
                    else {
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf("An error occurred"),
                            data = ""
                        ).let {
                            _verifyingCodeResult.postValue(
                                it
                            )
                        }
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    ).let {
                        _verifyingCodeResult.postValue(
                            it
                        )
                    }
                }
            } catch (e: Exception) {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                ).let {
                    _verifyingCodeResult.postValue(
                        it
                    )
                }
            }
        }
    }

    fun register(registerModel: UserRegisterDto){
        viewModelScope.launch {
            val response = authService.register(registerModel)
            if(response.isSuccessful){
                val responseBody = response.body()
                if (responseBody != null) {
                    if(responseBody.isSuccessful) {
                        BackResponse(
                            isSuccessful = true,
                            messages = responseBody.messages,
                            data = ""
                        ).let {
                            _registrationResult.postValue(
                                it
                            )
                        }
                    }
                    else {
                        BackResponse(
                            isSuccessful = false,
                            messages = responseBody.messages,
                            data = ""
                        ).let {
                            _registrationResult.postValue(
                                it
                            )
                        }
                    }
                }
                else {
                    BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    ).let {
                        _registrationResult.postValue(
                            it
                        )
                    }
                }
            }
            else {
                BackResponse(
                    isSuccessful = false,
                    messages = listOf("An error occurred"),
                    data = ""
                ).let {
                    _registrationResult.postValue(
                        it
                    )
                }
            }
        }
    }
}