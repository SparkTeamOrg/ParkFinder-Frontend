package com.app.parkfinder.ui.activities.statistic

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.StatisticDto
import com.app.parkfinder.logic.view_models.StatisticsViewModel
import com.app.parkfinder.ui.activities.parking.ReservationHistoryActivity
import com.app.parkfinder.ui.screens.statistic.UserStatisticsPage

import com.app.parkfinder.ui.theme.ParkFinderTheme

class StatisticsActivity: ComponentActivity() {
    private val statisticViewModel: StatisticsViewModel by viewModels()
    private var statistic = mutableStateOf<StatisticDto?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statisticViewModel.getUserStatistics()

        statisticViewModel.getStatistic.observe(this){res->
            if(res.isSuccessful)
                statistic.value = res.data
            else
                statistic.value = null
        }

        setContent {
            ParkFinderTheme {
                UserStatisticsPage(
                    statistic.value,
                    navigateToReservationHistory = { navigateToReservationHistory() }
                )
            }
        }
    }

    private fun navigateToReservationHistory() {
        val intent = Intent(this, ReservationHistoryActivity::class.java)
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        startActivity(intent, options.toBundle())
    }
}