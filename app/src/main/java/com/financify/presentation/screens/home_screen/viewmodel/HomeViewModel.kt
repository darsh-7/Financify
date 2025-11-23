package com.financify.presentation.screens.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.SavingGoalRepository
import com.financify.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val savingRepo: SavingGoalRepository,
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    val totalBalance: StateFlow<Double> = transactionRepo.getAllTransactions()
        .map { transactions ->
            transactions.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
                .coerceAtLeast(0.0)

        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)


    val totalIncome: StateFlow<Double> = transactionRepo.getAllTransactions()
        .map { transactions ->
            transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalOutcome: StateFlow<Double> = transactionRepo.getAllTransactions()
        .map { transactions ->
            transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val recentTransactions: StateFlow<List<Transaction>> = transactionRepo.getAllTransactions()
        .map { transactions -> transactions.sortedByDescending { it.date }.take(4) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSavings: StateFlow<List<SavingGoal>> = savingRepo.getAllGoals()
        .map { goals -> goals.take(4) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incomeSources: StateFlow<List<Transaction>> = transactionRepo.getAllTransactions()
        .map { transactions -> transactions.filter { it.type == TransactionType.INCOME } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
