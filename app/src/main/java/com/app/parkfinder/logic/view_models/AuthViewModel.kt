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
import java.util.logging.Logger

class AuthViewModel: ViewModel() {
    private val authService = RetrofitConfig.createService(AuthService::class.java)
    private val _loginResult = MutableLiveData<BackResponse<String>>()

    val loginResult: LiveData<BackResponse<String>> = _loginResult

    fun login(loginDto: UserLoginDto) {
        viewModelScope.launch {
            try {
                val response = authService.login(loginDto)

                if(response.isSuccessful) {
                    Logger.getLogger("AuthViewModel").info("Login successful")
                    response.body()?.let {
                        Logger.getLogger("AuthViewModel").info("Login result: $it")
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

    fun sendVerificationCode(email:String, code:String, navigateToVerifyCodePage : (BackResponse<String>) -> Unit)
    {
        viewModelScope.launch {
            val response = authService.emailVerification(email,code)
            if(response.isSuccessful){
                val res_b = response.body()
                res_b?.let(navigateToVerifyCodePage)
            }
            else
                println("bad request")
        }
    }

    fun verifyEmail(email:String, navigateToUserInfo : (BackResponse<String>) -> Unit)
    {
        viewModelScope.launch {
            val response = authService.verificationCodeRegister(email)
            if(response.isSuccessful){
                val res_b = response.body()
                res_b?.let(navigateToUserInfo)
            }
            else
                println("bad request za verifikaciju")
        }
    }

    fun register(registerm: UserRegisterDto){
        viewModelScope.launch {
            val response = authService.register(registerm)
            if(response.isSuccessful){
                println("good request")
            }
            else
                println("bad request")
        }
    }
}