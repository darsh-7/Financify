package com.financify.presentation.screens.savings_screen.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.financify.data.repository.SavingGoalRepository

class SavingGoalViewModelFactory(
    private val repository: SavingGoalRepository,
    private val goalId: String? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavingGoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavingGoalViewModel(repository, goalId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
