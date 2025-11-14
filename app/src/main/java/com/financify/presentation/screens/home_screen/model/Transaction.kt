package com.financify.presentation.screens.home_screen.model

import java.time.LocalDate

data class Transaction(
    val id: String,
    val name: String,
    val description: String,
    val amount: Double,
    val date: LocalDate,
    val isIncome: Boolean
)