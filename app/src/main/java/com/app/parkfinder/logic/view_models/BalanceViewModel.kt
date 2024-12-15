package com.app.parkfinder.logic.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.paging.TransactionPagingSource
import com.app.parkfinder.logic.services.BalanceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BalanceViewModel() : ViewModel() {

    private val balanceService = RetrofitConfig.createService(BalanceService::class.java)

    private val _balance = MutableStateFlow<Double?>(null)
    val balance: StateFlow<Double?> = _balance

    val transactions = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { TransactionPagingSource(balanceService) }
    ).flow.cachedIn(viewModelScope)

    init {
        fetchBalance()
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
}