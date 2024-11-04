package com.app.parkfinder.logic.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.UserLoginDto
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    fun login(loginDto: UserLoginDto, navigateToLandingPage : (BackResponse<String>) -> Unit) {
        viewModelScope.launch {
            val response = RetrofitConfig.authService.login(loginDto)
            if(response.isSuccessful){
                val res_b = response.body()
                res_b?.let(navigateToLandingPage)
            }
            else
                println("request je neuspesan")
        }
    }

}