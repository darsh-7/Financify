package com.financify.data.repository



import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.data.data_sources.local.room.entities.SavingGoalDao
import kotlinx.coroutines.flow.Flow

class SavingGoalRepository(private val dao: SavingGoalDao) {

    suspend fun insertGoal(goal: SavingGoal) = dao.insertGoal(goal)

    suspend fun updateGoal(goal: SavingGoal) = dao.updateGoal(goal)

    suspend fun deleteGoal(goalId: String) = dao.deleteGoal(goalId)

    fun getAllGoals(): Flow<List<SavingGoal>> = dao.getAllGoals()
    fun getGoalById(goalId: String): Flow<SavingGoal?> {

        return dao.getGoalById(goalId)
    }

//    suspend fun getGoalById(goalId: String): SavingGoal? = dao.getGoalById(goalId)
}
