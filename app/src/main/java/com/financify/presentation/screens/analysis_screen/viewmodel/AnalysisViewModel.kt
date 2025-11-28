package com.financify.presentation.screens.analysis_screen.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financify.data.repository.TransactionRepository
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.presentation.model.MonthlyStats
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AnalysisViewModel(private val repo: TransactionRepository) : ViewModel() {
    private val _selectedMonths = MutableStateFlow(1)
    fun setMonths(months: Int) {
        Log.d("AnalyticsDebug", "setMonths called -> $months")
        _selectedMonths.value = months
    }

    private val monthFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    val monthlyStats: StateFlow<List<MonthlyStats>> = _selectedMonths
        .onEach { months -> Log.d("AnalyticsDebug", "selectedMonths emitted = $months") }
        .flatMapLatest { months ->
            val (start, end) = computePeriod(months)
//            Log.d("AnalyticsDebug", "Requesting transactions between ${Date(start)} and ${Date(end)}")

            repo.getTransactionsBetweenDates(start, end)
                .onEach { txs -> Log.d("AnalyticsDebug", "repo returned ${txs.size} transactions for months=$months") }
                .map { txs ->
                    val stats = computeMonthlyStats(txs)
                    Log.d("AnalyticsDebug", "computed ${stats.size} monthlyStats for months=$months -> $stats")
                    stats
                }
        }
        .catch { e ->
            Log.e("AnalyticsDebug", "monthlyStats flow error", e)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun computePeriod(months: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance()

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val end = cal.timeInMillis

        cal.add(Calendar.MONTH, -months + 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)

        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val start = cal.timeInMillis

        return start to end
    }

    private fun computeMonthlyStats(transactions: List<Transaction>): List<MonthlyStats> {
        if (transactions.isEmpty()) return emptyList()

        val grouped = transactions.groupBy {
            val c = Calendar.getInstance().apply { timeInMillis = it.date }
            "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH)}"
        }

        return grouped.map { (_, list) ->
            val sampleDate = Date(list.first().date)
            val monthName = monthFormatter.format(sampleDate)
            MonthlyStats(
                monthName = monthName,
                income = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                expense = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            )
        }.sortedBy { it.monthName }
    }
}
