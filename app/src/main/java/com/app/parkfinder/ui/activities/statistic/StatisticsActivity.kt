package com.app.parkfinder.ui.activities.statistic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.logic.models.dtos.StatisticDto
import com.app.parkfinder.logic.view_models.StatisticsViewModel
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
                UserStatisticsPage(statistic.value)
            }
        }
    }
}