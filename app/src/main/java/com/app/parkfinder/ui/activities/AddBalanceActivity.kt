package com.app.parkfinder.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.app.parkfinder.ui.screens.main.AddBalanceScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme
import com.app.parkfinder.logic.view_models.BalanceViewModel

class AddBalanceActivity : BaseActivity() {

    private val balanceViewModel: BalanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                AddBalanceScreen(
                    onAddBalance = { amount ->
                        balanceViewModel.addBalance(amount)
                        finish()
                    },
                    onBackClick = { finish() }
                )
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AddBalanceActivity::class.java)
        }
    }
}