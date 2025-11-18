package com.financify.presentation.screens.transaction_screen.transactions

data class TransactionData(
    val name: String,
    val description: String,
    val value: Int,
    val timestamp: Long
)