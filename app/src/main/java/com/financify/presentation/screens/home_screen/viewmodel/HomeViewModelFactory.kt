package com.financify.presentation.screens.home_screen.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.financify.data.data_sources.local.room.AppDatabase
import com.financify.data.repository.SavingGoalRepository
import com.financify.data.repository.TransactionRepository

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val database = AppDatabase.getDatabase(context.applicationContext)
            val transactionRepo = TransactionRepository(database.TransactionDao ())
            val savingRepo = SavingGoalRepository(database.savingGoalDao ())

            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(savingRepo, transactionRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}