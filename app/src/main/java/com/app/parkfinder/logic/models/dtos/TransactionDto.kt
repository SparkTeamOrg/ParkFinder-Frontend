package com.app.parkfinder.logic.models.dtos

data class TransactionDto (
    val id: Int,
    val userId: Int,
    val amount: Double,
    val date: String
)