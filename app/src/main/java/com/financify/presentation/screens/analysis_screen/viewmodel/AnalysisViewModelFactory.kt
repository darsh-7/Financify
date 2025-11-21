package com.financify.presentation.screens.analysis_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.financify.data.repository.TransactionRepository

class AnalysisViewModelFactory(
    private val repo: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnalysisViewModel(repo) as T
    }
}
