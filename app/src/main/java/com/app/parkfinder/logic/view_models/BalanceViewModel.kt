package com.app.parkfinder.logic.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.app.parkfinder.logic.RetrofitConfig
import com.app.parkfinder.logic.models.dtos.AddTransactionDto
import com.app.parkfinder.logic.paging.TransactionPagingSource
import com.app.parkfinder.logic.services.BalanceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BalanceViewModel() : ViewModel() {

    private val balanceService = RetrofitConfig.createService(BalanceService::class.java)

    private val _balance = MutableStateFlow<Double?>(null)
    val balance: StateFlow<Double?> = _balance

    private val _transactionPagingSource = MutableStateFlow<TransactionPagingSource?>(null)

    val transactions = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            TransactionPagingSource(balanceService).also {
                _transactionPagingSource.value = it
            }
        }
    ).flow.cachedIn(viewModelScope)

    init {
        fetchBalance()
    }

    fun fetchBalance() {
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

    fun addBalance(amount: Double) {

        if(amount <= 0) {
            Log.i("BalanceViewModel", "Invalid amount")
            return
        }

        viewModelScope.launch {
            try {
                val model = AddTransactionDto(
                    userId = -1,
                    amount = amount,
                )

                val response = balanceService.addBalance(model)

                if (response.isSuccessful && response.body()?.isSuccessful == true) {
                    fetchBalance()
                    refreshTransactions()   // Refresh transactions after adding balance
                } else {
                    Log.i("BalanceViewModel", "Error adding balance: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.i("BalanceViewModel", "Error adding balance: $e")
            }
        }
    }

    fun refreshTransactions() {
        _transactionPagingSource.value?.invalidate()
    }

}