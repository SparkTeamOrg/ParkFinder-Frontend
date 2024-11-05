package com.app.parkfinder.logic.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import com.app.parkfinder.logic.models.dtos.UserRegisterDto
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Body

class AuthViewModel: ViewModel() {
    fun login(loginDto: UserLoginDto, navigateToLandingPage : (BackResponse<String>) -> Unit) {
        viewModelScope.launch {
            val response = RetrofitConfig.authService.login(loginDto)
            if(response.isSuccessful){
                val res_b = response.body()
                res_b?.let(navigateToLandingPage)
            }
            else
                println("bad request")
        }
    }

    fun sendVerificationCode(email:String, code:String, navigateToVerifyCodePage : (BackResponse<String>) -> Unit)
    {
        viewModelScope.launch {
            val response = RetrofitConfig.authService.emailVerification(email,code)
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
            val response = RetrofitConfig.authService.verificationCodeRegister(email)
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
            val response = RetrofitConfig.authService.register(registerm)
            if(response.isSuccessful){
                println("good request")
            }
            else
                println("bad request")
        }
    }

}