package com.app.parkfinder.ui.screens.statistic

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.app.parkfinder.R
import com.app.parkfinder.logic.enums.MonthEnum
import com.app.parkfinder.logic.models.dtos.StatisticDto
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlin.math.round

@SuppressLint("DefaultLocale")
@Composable
fun UserStatisticsPage(
    statistic: StatisticDto,
    navigateToReservationHistory: () -> Unit,
    isLoading: Boolean,
    onBackClick: () -> Unit
) {
    val darkBackground = Color(0xFF1C1C1E)
    val cardBackground = Color(0xFF2C2C2E)
    val highlightColor = Color(0xFF00E5FF)

    val scrollState = rememberScrollState()

    // Mock Data
    val totalMoneySpent = "${round(statistic.totalMoneySpent * 100) / 100} RSD"
    val avgTimeFinding = "${round(statistic.averageReservationTime / 60 * 100) / 100} min"
    val totalReservations = "${statistic.reservationCount}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF293038), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.park_finder_logo),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Dummy",
                tint = Color.Transparent,
                modifier = Modifier.size(60.dp)
            )
        }

        if(!isLoading) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Center
                ){
                    Text(
                        text = "Statistics",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Top Metric
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircleCard(
                        "Total Reservations",
                        totalReservations,
                        darkBackground,
                        highlightColor
                    )
                }

                // Middle Metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatisticCard(
                        "Total Money Spent",
                        totalMoneySpent,
                        cardBackground,
                        highlightColor
                    )
                    StatisticCard(
                        "Avg. Finding Time",
                        avgTimeFinding,
                        cardBackground,
                        highlightColor
                    )
                }

                // Bottom Button
                Button(
                    onClick = { navigateToReservationHistory() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 10.dp)
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        "See Reservation History",
                        color = White,
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Chart 1: Total Money Spent in Last 6 Months (Multi-Bar)
                Text(
                    "Money Spent in Last 6 Months",
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                val months = statistic.months.map { month -> MonthEnum.fromValue(month)!!.name }


                val moneySpentStatistic = statistic.vehicles.map { vehicle ->
                    vehicle.moneySpentPerMonth.map { spent ->
                        (round(spent * 100) / 100).toFloat()
                    }
                }
                val reservationCountStatistic = statistic.vehicles.map { vehicle ->
                    vehicle.reservationCountPerMonth.map { count ->
                        count.toFloat()
                    }
                }

                val labels = statistic.vehicles.map { vehicle ->
                    "${vehicle.brandName} ${vehicle.modelName}"
                }

                MultiBarChartComposable(
                    xLabels = months,
                    vehiclesData = moneySpentStatistic,
                    labels

                )

                Spacer(modifier = Modifier.height(30.dp))

                // Chart 2: Reservations Per Vehicle in Last 6 Months
                Text(
                    "Reservations Per Vehicle in Last 6 Months",
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                MultiBarChartComposable(
                    xLabels = months,
                    vehiclesData = reservationCountStatistic,
                    labels
                )
            }
        }
        else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(darkBackground),
                contentAlignment = Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(
                        100.dp
                    )
                )
            }
        }
    }

}

@Composable
fun CircleCard(title: String, value: String, backgroundColor: Color, highlightColor: Color, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(1000.dp),
        modifier = modifier.padding(8.dp)
            .border(8.dp, highlightColor, RoundedCornerShape(1000.dp))
            .size(180.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = Color.LightGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = highlightColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatisticCard(title: String, value: String, backgroundColor: Color, highlightColor: Color, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.padding(8.dp)
            .width(175.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = Color.LightGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = highlightColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MultiBarChartComposable(xLabels: List<String>, vehiclesData: List<List<Float>>, vehicleLabels: List<String>) {
    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                val barDataSets = mutableListOf<BarDataSet>()

                vehiclesData.forEachIndexed { index, vehicleData ->
                    val entries = vehicleData.mapIndexed { i, value -> BarEntry(i.toFloat(), value) }
                    val dataSet = BarDataSet(entries, vehicleLabels[index]).apply {
                        color = when (index) {
                            0 -> Color.Cyan.hashCode()
                            1 -> Color.Magenta.hashCode()
                            2 -> Color.Yellow.hashCode()
                            3 -> Color.Red.hashCode()
                            4 -> Color.Blue.hashCode()
                            else -> Color.Green.hashCode()
                        }
                        valueTextColor = White.hashCode()
                    }
                    barDataSets.add(dataSet)
                }

                val barData = BarData(barDataSets as List<IBarDataSet>?).apply {
                    barWidth = 0.2f
                }

                val groupSpace = 0.08f
                val barSpace = 0.03f

                data = barData
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(xLabels)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setCenterAxisLabels(true)
                    textColor = White.hashCode()
                }
                axisLeft.textColor = White.hashCode()
                axisRight.isEnabled = false
                legend.apply {
                    textColor = White.hashCode()
                    setExtraOffsets(0f, 0f, 0f, 8f)
                }
                description.isEnabled = false

                setTouchEnabled(false)
                animateY(1000)

                // Adjust bar group spacing
                barData.barWidth = 0.2f
                if(vehiclesData.size > 1)
                    groupBars(0f, groupSpace, barSpace)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
