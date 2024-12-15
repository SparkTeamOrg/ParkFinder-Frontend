package com.app.parkfinder.ui.screens.statistic

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.app.parkfinder.logic.enums.MonthEnum
import com.app.parkfinder.logic.models.dtos.StatisticDto
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


@SuppressLint("DefaultLocale")
@Composable
fun UserStatisticsPage(
    statistic: StatisticDto?,
    navigateToReservationHistory: () -> Unit
) {
    val darkBackground = Color(0xFF1C1C1E)
    val cardBackground = Color(0xFF2C2C2E)
    val highlightColor = Color(0xFF00E5FF)

    val scrollState = rememberScrollState()

    if(statistic!=null) {
        // Mock Data
        val totalMoneySpent = "${String.format("%.2f", statistic.totalMoneySpent).toDouble()} din"
        val avgTimeFinding =
            "${String.format("%.2f", statistic.averageReservationTime / 60).toDouble()} min"
        val totalReservations = "${statistic.reservationCount}"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBackground)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticCard("Total Money Spent", totalMoneySpent, cardBackground, highlightColor)
                StatisticCard("Avg. Finding Time", avgTimeFinding, cardBackground, highlightColor)
            }

            // Middle Metric
            StatisticCard(
                "Total Reservations", totalReservations, cardBackground, highlightColor,
                modifier = Modifier.fillMaxWidth()
            )

            // Chart 1: Total Money Spent in Last 6 Months (Multi-Bar)
            Text(
                "Money Spent in Last 6 Months",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            val months = statistic.months.map { month -> MonthEnum.fromValue(month)!!.name }



            val moneySpentStatistic = statistic.vehicles.map { vehicle ->
                vehicle.moneySpentPerMonth.map { spent ->
                    String.format("%.2f", spent).toFloat()
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
                title = "Money Spent",
                xLabels = months,
                vehiclesData = moneySpentStatistic,
                labels

            )

            // Chart 2: Reservations Per Vehicle in Last 6 Months
            Text(
                "Reservations Per Vehicle in Last 6 Months",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            MultiBarChartComposable(
                title = "Reservations",
                xLabels = months,
                vehiclesData = reservationCountStatistic,
                labels
            )

            // Bottom Button
            Button(
                onClick = { navigateToReservationHistory() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = highlightColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "See Reservation History",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatisticCard(title: String, value: String, backgroundColor: Color, highlightColor: Color, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.padding(8.dp)
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
fun MultiBarChartComposable(title: String, xLabels: List<String>, vehiclesData: List<List<Float>>, vehicleLabels: List<String>) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                val barDataSets = mutableListOf<BarDataSet>()

                // Grouped Bar Data
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
                        valueTextColor = Color.White.hashCode()
                    }
                    barDataSets.add(dataSet)
                }

                val barData = BarData(barDataSets as List<IBarDataSet>?).apply {
                    barWidth = 0.2f // Width of each bar
                }

                // Group bars together
                val groupSpace = 0.08f
                val barSpace = 0.03f
                val groupCount = xLabels.size

                data = barData
                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(xLabels)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setCenterAxisLabels(true)
                    textColor = Color.White.hashCode()
                }
                axisLeft.textColor = Color.White.hashCode()
                axisRight.isEnabled = false
                legend.textColor = Color.White.hashCode()
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
