package com.financify.presentation.screens.transaction_screen.transactions

sealed class TransactionFilter(val displayName: String) {
    object All : TransactionFilter("All")
    object Income : TransactionFilter("Income")
    object Expenses : TransactionFilter("Expenses")
    object Recent : TransactionFilter("Recent")
    object Oldest : TransactionFilter("Oldest")
}
