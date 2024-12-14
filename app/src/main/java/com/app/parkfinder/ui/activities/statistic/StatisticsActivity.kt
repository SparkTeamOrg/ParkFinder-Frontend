package com.app.parkfinder.ui.activities.statistic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.parkfinder.ui.screens.statistic.UserStatisticsPage

import com.app.parkfinder.ui.theme.ParkFinderTheme

class StatisticsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ParkFinderTheme {
                UserStatisticsPage()
            }
        }
    }
}