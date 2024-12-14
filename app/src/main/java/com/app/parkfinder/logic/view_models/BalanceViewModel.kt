package com.app.parkfinder.logic.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.dtos.TransactionDto
import com.app.parkfinder.logic.services.BalanceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BalanceViewModel() : ViewModel() {

    private val balanceService = RetrofitConfig.createService(BalanceService::class.java)

    private val _balance = MutableStateFlow<Double?>(null)
    val balance: StateFlow<Double?> = _balance

    private val _transactions = MutableStateFlow<List<TransactionDto>?>(null)
    val transactions: StateFlow<List<TransactionDto>?> = _transactions

    init {
        fetchBalance()
        fetchTransactions()
    }

    private fun fetchBalance() {
        viewModelScope.launch {
            try {
                val response = balanceService.getUserBalance()

                if(response.isSuccessful) {
                    if (response.body()?.isSuccessful == true) {
                        _balance.value = response.body()?.data
                    }
                    else {
                        // Handle error
                        _balance.value = null
                        Log.i("BalanceViewModel", "Error fetching balance")
                    }
                }
                else {
                    // Handle error
                    _balance.value = null
                    Log.i("BalanceViewModel", "Error fetching balance: ${response.errorBody()}")
                }

            } catch (e: Exception) {
                // Handle error
                _balance.value = null
                Log.i("BalanceViewModel", "Error fetching balance: $e")
            }
        }
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            try {
                val response = balanceService.getUserTransactions()

                if(response.isSuccessful) {
                    if (response.body()?.isSuccessful == true) {
                        _transactions.value = response.body()?.data
                    }
                    else {
                        // Handle error
                        _transactions.value = null
                        Log.i("BalanceViewModel", "Error fetching transactions")
                    }
                }
                else {
                    // Handle error
                    _transactions.value = null
                    Log.i("BalanceViewModel", "Error fetching transactions: ${response.errorBody()}")
                }

            } catch (e: Exception) {
                // Handle error
                _transactions.value = null
                Log.i("BalanceViewModel", "Error fetching transactions: $e")
            }
        }
    }
}