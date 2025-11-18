package com.financify.presentation.screens.savings_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financify.data.repository.SavingGoalRepository
import com.financify.data.data_sources.local.room.entities.SavingGoal
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class SavingGoalViewModel(
    private val repository: SavingGoalRepository,
    private val goalId: String? = null
) : ViewModel() {

    private val _currentGoal = MutableStateFlow<SavingGoal?>(null)
    val currentGoal: StateFlow<SavingGoal?> = _currentGoal.asStateFlow()

    val allGoals: StateFlow<List<SavingGoal>> = repository.getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalStats: StateFlow<GoalStats> = allGoals.map { goals ->
        val totalSaved = goals.sumOf { it.savedAmount }
        val totalTarget = goals.sumOf { it.targetAmount }
        val totalGoals = goals.size
        val totalProgress = if (totalTarget > 0.0) (totalSaved / totalTarget) * 100 else 0.0

        GoalStats(
            totalSaved = totalSaved,
            totalTarget = totalTarget,
            totalGoals = totalGoals,
            totalProgress = totalProgress
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GoalStats()
    )

    init {
        if (!goalId.isNullOrEmpty() && goalId != "-1") {
            loadGoal(goalId)
        }
    }

    private fun loadGoal(id: String) {
        viewModelScope.launch {
            repository.getGoalById(id).collectLatest { goal ->
                _currentGoal.value = goal
            }
        }
    }

    fun saveGoal(
        userId: String,
        goalName: String,
        targetAmount: Double,
        savedAmount: Double,
        goalType: String,
        selectedDate: String,
        note: String,
        color: String,
        icon: String
    ) {
        val existingId = _currentGoal.value?.id

        val goal = SavingGoal(
            id = existingId ?: UUID.randomUUID().toString(),
            userId = userId,
            goalName = goalName,
            targetAmount = targetAmount,
            savedAmount = savedAmount,
            goalType = goalType,
            selectedDate = selectedDate,
            note = note,
            color = color,
            icon = icon
        )

        viewModelScope.launch {
            repository.insertGoal(goal)
        }
    }

    fun deleteGoal(goalId: String) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
        }
    }
}

data class GoalStats(
    val totalSaved: Double = 0.0,
    val totalTarget: Double = 0.0,
    val totalGoals: Int = 0,
    val totalProgress: Double = 0.0
)