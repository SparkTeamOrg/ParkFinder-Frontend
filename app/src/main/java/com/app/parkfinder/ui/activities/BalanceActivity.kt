package com.app.parkfinder.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.app.parkfinder.ui.screens.main.BalanceScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.logic.view_models.BalanceViewModel

class BalanceActivity : BaseActivity() {

    private val balanceViewModel: BalanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {

                val balance = balanceViewModel.balance.collectAsState().value ?: 0.0
                val transactions = balanceViewModel.transactions.collectAsLazyPagingItems()

                BalanceScreen(
                    balance = balance,
                    onPreviewPaymentClick = {
                        startActivity(AddBalanceActivity.createIntent(this))
                    },
                    transactions = transactions,
                    onBackClick = { finish() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        balanceViewModel.fetchBalance()
        balanceViewModel.refreshTransactions()
    }
}