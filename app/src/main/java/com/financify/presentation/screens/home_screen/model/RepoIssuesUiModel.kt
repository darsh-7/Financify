package com.financify.presentation.screens.cam_scan_screen.model

data class RepoIssuesUiModel(
    val id:Long,
    val title: String,
    val state : String,
    val description: String,
    val createdAt: String
)

//todo: add ths code back when dashboard is implemented

//import com.financify.data.data_sources.local.room.entities.SavingGoal
//import com.financify.data.data_sources.local.room.entities.Transaction
//
//
//// This is a simplified state holder for the dashboard screen
//data class DashboardUiState(
//    val totalBalance: Double = 0.0,
//    val totalIncome: Double = 0.0,
//    val totalOutcome: Double = 0.0,
//    val recentTransactions: List<Transaction> = emptyList(),
//    val savingGoals: List<SavingGoal> = emptyList(),
//    val isLoading: Boolean = true
//)

