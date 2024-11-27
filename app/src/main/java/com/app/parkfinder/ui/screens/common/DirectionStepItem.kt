package com.app.parkfinder.ui.screens.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.parkfinder.logic.models.NavigationStep

@SuppressLint("DefaultLocale")
@Composable
fun DirectionStepItem(step: NavigationStep, stepIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step number
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.Blue, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (stepIndex + 1).toString(),
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Instruction and metadata
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = step.instruction,
                style = MaterialTheme.typography.body1,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Distance: ${step.distance.toInt()} m · Duration: ${String.format("%.2f",(step.duration / 60))} min",
                style = MaterialTheme.typography.caption,
                color = Color.White
            )
        }
    }
}
@Composable
fun DirectionsPanel(steps: List<NavigationStep>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Navigation Instructions",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            itemsIndexed(steps) { index, step ->
                DirectionStepItem(step = step, stepIndex = index)
            }
        }
    }
}
