package com.app.parkfinder.logic.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.ResetPasswordDto
import com.app.parkfinder.logic.models.dtos.TokenDto
import com.app.parkfinder.logic.models.dtos.UpdateUserNameDto
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.models.dtos.UserRegisterDto
import com.app.parkfinder.logic.services.AuthService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AuthViewModel: ViewModel() {
    private val authService = RetrofitConfig.createService(AuthService::class.java)
    private val _loginResult = MutableLiveData<BackResponse<TokenDto>>()
    private val _sendingVerificationCodeForRegistrationResult = MutableLiveData<BackResponse<String>>()
    private val _sendingVerificationCodeForPasswordResetResult = MutableLiveData<BackResponse<String>>()
    private val _verifyingCodeResult = MutableLiveData<BackResponse<String>>()
    private val _registrationResult = MutableLiveData<BackResponse<String>>()
    private val _passwordResetResult = MutableLiveData<BackResponse<String>>()
    private val _updateUserNameResult = MutableLiveData<BackResponse<String>>()

    val loginResult: LiveData<BackResponse<TokenDto>> = _loginResult
    val sendingVerificationCodeForRegistrationResult: LiveData<BackResponse<String>> = _sendingVerificationCodeForRegistrationResult
    val sendingVerificationCodeForPasswordResetResult: LiveData<BackResponse<String>> = _sendingVerificationCodeForPasswordResetResult
    val verifyingCodeResult: LiveData<BackResponse<String>> = _verifyingCodeResult
    val registrationResult: LiveData<BackResponse<String>> = _registrationResult
    val passwordResetResult: LiveData<BackResponse<String>> = _passwordResetResult
    val updateUserNameResult: LiveData<BackResponse<String>> = _updateUserNameResult

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
                        data = TokenDto("", "")
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
                        BackResponse(
                            isSuccessful = false,
                            messages = listOf(e.message ?: "An error occurred"),
                            data = TokenDto("", "")
                        )
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

    fun sendVerificationCodeForPasswordReset(email: String) {
        viewModelScope.launch {
            try {
                val response = authService.sendVerificationCodeForPasswordReset(email)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.isSuccessful) {
                            BackResponse(
                                isSuccessful = true,
                                messages = responseBody.messages,
                                data = ""
                            ).let {
                                _sendingVerificationCodeForPasswordResetResult.postValue(
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
                                _sendingVerificationCodeForPasswordResetResult.postValue(
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
                            _sendingVerificationCodeForPasswordResetResult.postValue(
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
                        _sendingVerificationCodeForPasswordResetResult.postValue(
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
                    _sendingVerificationCodeForPasswordResetResult.postValue(
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
            val response = authService.register(
                firstName = MultipartBody.Part.createFormData("firstName", registerModel.firstName),
                lastName = MultipartBody.Part.createFormData("lastName", registerModel.lastName),
                email = MultipartBody.Part.createFormData("email", registerModel.email),
                mobilePhone = MultipartBody.Part.createFormData("mobilePhone", registerModel.mobilePhone),
                password = MultipartBody.Part.createFormData("password", registerModel.password),
                licencePlate = MultipartBody.Part.createFormData("licencePlate", registerModel.licencePlate),
                color = MultipartBody.Part.createFormData("color", registerModel.color),
                modelId = MultipartBody.Part.createFormData("modelId", registerModel.modelId.toString()),
                verificationCode = MultipartBody.Part.createFormData("verificationCode", registerModel.verificationCode),
                profileImage = registerModel.profileImage
            )
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

    fun resetPassword(resetPasswordDto: ResetPasswordDto) {
        viewModelScope.launch {
            try {
                val response = authService.resetPassword(resetPasswordDto)

                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if(responseBody.isSuccessful) {
                            BackResponse(
                                isSuccessful = true,
                                messages = responseBody.messages,
                                data = ""
                            ).let {
                                _passwordResetResult.postValue(
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
                                _passwordResetResult.postValue(
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
                            _passwordResetResult.postValue(
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
                        _passwordResetResult.postValue(
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
                    _passwordResetResult.postValue(
                        it
                    )
                }
            }
        }
    }

    fun updateUserName(updateUserNameDto: UpdateUserNameDto) {
        viewModelScope.launch {
            try {
                val response = authService.updateUser(updateUserNameDto)

                if(response.isSuccessful) {
                    response.body()?.let {
                        _updateUserNameResult.postValue(it)
                    }
                }
                else {
                    val errorResponse = BackResponse(
                        isSuccessful = false,
                        messages = listOf("An error occurred"),
                        data = ""
                    )
                    _updateUserNameResult.postValue(errorResponse)
                }
            } catch (e: Exception) {
                val errorResponse = BackResponse(
                    isSuccessful = false,
                    messages = listOf(e.message ?: "An error occurred"),
                    data = ""
                )
                _updateUserNameResult.postValue(errorResponse)
            }
        }
    }
}