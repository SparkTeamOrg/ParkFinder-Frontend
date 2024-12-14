package com.app.parkfinder.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.parkfinder.R
import com.app.parkfinder.logic.models.dtos.TransactionDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

@Composable
fun BalanceScreen(
    balance: Double,
    onPreviewPaymentClick: () -> Unit,
    transactionHistory: List<TransactionDto>,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF151A24))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Current Balance",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${round(balance * 100) / 100} RSD",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (balance < 0) Color.Red else Color(0xFF0FCFFF)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onPreviewPaymentClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0FCFFF),
                contentColor = Color.White
            )
        ) {
            Text("Preview Payment Check")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Transaction History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            transactionHistory.forEach { transaction ->
                TransactionItem(transaction)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF293038), shape = MaterialTheme.shapes.medium)
            .padding(10.dp)
    ) {
        Text(
            text = LocalDateTime.parse(transaction.date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (transaction.amount < 0) "Parking Fee" else "Payment to Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = if (transaction.amount > 0) "+" + transaction.amount.toString() + " RSD" else transaction.amount.toString() + " RSD",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.amount > 0) Color(0xFF0FCFFF) else Color.Red
            )
        }
    }
}

data class Transaction(
    val date: String,
    val description: String,
    val amount: String,
    val isCredit: Boolean
)

@Preview(showBackground = true)
@Composable
fun BalanceScreenPreview() {
    val transactions = listOf(
        TransactionDto(1, 1, -100.1, "2021-01-01"),
        TransactionDto(2, 1, 100.4, "2021-01-02"),
    )
    BalanceScreen(
        balance = 100.00,
        onPreviewPaymentClick = {},
        transactionHistory = transactions,
        onBackClick = {}
    )
}