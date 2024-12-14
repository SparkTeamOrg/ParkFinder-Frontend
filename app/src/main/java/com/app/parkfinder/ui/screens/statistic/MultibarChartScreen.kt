package com.app.parkfinder.ui.screens.statistic

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.parkfinder.logic.view_models.StatisticsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import okhttp3.internal.wait

@Composable
fun UserStatisticsPage(
    statisticsModel: StatisticsViewModel = viewModel()
) {
    val darkBackground = Color(0xFF1C1C1E)
    val cardBackground = Color(0xFF2C2C2E)
    val highlightColor = Color(0xFF00E5FF)

    val scrollState = rememberScrollState()

    // Mock Data
    val totalMoneySpent = "$1,500"
    val avgTimeFinding = "12 min"
    val totalReservations = "35"

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
        MultiBarChartComposable(
            title = "Money Spent",
            xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
            vehiclesData = listOf(
                listOf(200f, 150f, 300f, 250f, 400f, 350f), // Vehicle 1
                listOf(180f, 120f, 270f, 220f, 390f, 320f), // Vehicle 2
                listOf(160f, 140f, 280f, 200f, 410f, 360f)  // Vehicle 3
            )
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
            xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
            vehiclesData = listOf(
                listOf(10f, 15f, 20f, 18f, 25f, 22f), // Vehicle 1
                listOf(8f, 12f, 16f, 14f, 20f, 19f),  // Vehicle 2
                listOf(6f, 10f, 14f, 12f, 18f, 15f)   // Vehicle 3
            )
        )

        // Bottom Button
        Button(
            onClick = { /* Navigate to Reservation History */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = highlightColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("See Reservation History", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
fun MultiBarChartComposable(title: String, xLabels: List<String>, vehiclesData: List<List<Float>>) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                val barDataSets = mutableListOf<BarDataSet>()

                // Grouped Bar Data
                vehiclesData.forEachIndexed { index, vehicleData ->
                    val entries = vehicleData.mapIndexed { i, value -> BarEntry(i.toFloat(), value) }
                    val dataSet = BarDataSet(entries, "Vehicle ${index + 1}").apply {
                        color = when (index) {
                            0 -> Color.Cyan.hashCode()
                            1 -> Color.Magenta.hashCode()
                            else -> Color.Yellow.hashCode()
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
                groupBars(0f, groupSpace, barSpace)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
