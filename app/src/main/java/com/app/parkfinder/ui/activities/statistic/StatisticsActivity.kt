package com.app.parkfinder.ui.activities.statistic

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.BackResponse
import com.app.parkfinder.logic.models.dtos.StatisticDto
import com.app.parkfinder.logic.view_models.StatisticsViewModel
import com.app.parkfinder.ui.activities.BaseActivity
import com.app.parkfinder.ui.activities.parking.ReservationHistoryActivity
import com.app.parkfinder.ui.screens.statistic.UserStatisticsPage

import com.app.parkfinder.ui.theme.ParkFinderTheme

class StatisticsActivity: BaseActivity() {
    private val statisticViewModel: StatisticsViewModel by viewModels()
    private var isLoading = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isLoading.value = true
        statisticViewModel.getUserStatistics()

        setContent {
            val statistic = statisticViewModel.getStatistic.observeAsState(
                BackResponse(isSuccessful = false, messages = emptyList(), data = StatisticDto(emptyList(), emptyList(), 0.0, 0,0.0))
            )

            if(statistic.value.isSuccessful)
                isLoading.value = false

            ParkFinderTheme {
                UserStatisticsPage(
                    statistic.value.data,
                    navigateToReservationHistory = { navigateToReservationHistory() },
                    isLoading = isLoading.value,
                    onBackClick = { finish() }
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