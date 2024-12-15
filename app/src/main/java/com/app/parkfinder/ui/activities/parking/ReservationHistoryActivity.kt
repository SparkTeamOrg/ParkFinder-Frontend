package com.app.parkfinder.ui.activities.parking

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.screens.main.ReservationHistoryScreen
import com.app.parkfinder.ui.theme.ParkFinderTheme

class ReservationHistoryActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            ParkFinderTheme {
                ReservationHistoryScreen(
                    onBackClick = { finish() }
                )
            }
        }

    }
}