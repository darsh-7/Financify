package com.financify.data.data_sources.local.room.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financify.data.data_sources.local.room.entities.SavingGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingGoal)

    @Update
    suspend fun updateGoal(goal: SavingGoal)

    @Query("DELETE FROM saving_goals WHERE id = :goalId")
    suspend fun deleteGoal(goalId: String)

    @Query("SELECT * FROM saving_goals")


    fun getAllGoals(): Flow<List<SavingGoal>>

    @Query("SELECT * FROM saving_goals WHERE id = :goalId")
    fun getGoalById(goalId: String): Flow<SavingGoal?>
}
