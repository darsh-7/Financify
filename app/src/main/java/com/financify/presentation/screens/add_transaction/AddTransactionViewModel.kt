package com.financify.presentation.screens.add_transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    var type by mutableStateOf(TransactionType.INCOME)
    var amount by mutableStateOf("")
    var title by mutableStateOf("")
    var selectedDate by mutableStateOf<Long?>(null)
    var description by mutableStateOf("")
    var isCategory by mutableStateOf(false)

    // Get categories based on the selected type
    @OptIn(ExperimentalCoroutinesApi::class)
    val categories: StateFlow<List<Transaction>> = snapshotFlow { type }.flatMapLatest { type ->
            repository.getCategoriesByType(type)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // Save transaction and return its ID
    suspend fun saveTransactionAndGetId(): String {
        val transaction = Transaction(
            type = type,
            amount = amount.toDoubleOrNull() ?: 0.0,
            title = title,
            date = selectedDate ?: System.currentTimeMillis(),
            description = description,
            isCategory = isCategory
        )
        repository.insertTransaction(transaction)
        return transaction.id
    }

    // Fill form with transaction data
    fun fillFormWithTransaction(transaction: Transaction) {
        type = transaction.type
        amount = transaction.amount.toString()
        title = transaction.title
        description = transaction.description
    }

    // Get transaction by ID
    suspend fun getTransactionById(id: String): Transaction? {
        return repository.getTransactionById(id)
    }

    // Handle date selection
    fun onDateSelected(timestamp: Long) {
        selectedDate = timestamp
    }

    // Format date for display
    fun formatDate(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val zoneId = ZoneId.systemDefault() // local timezone
        val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy h:mm a")
        return localDateTime.format(formatter)
    }

    // Clear form fields
    fun clearForm() {
        type = TransactionType.INCOME
        amount = ""
        title = ""
        description = ""
        isCategory = false
        selectedDate = null
    }
}