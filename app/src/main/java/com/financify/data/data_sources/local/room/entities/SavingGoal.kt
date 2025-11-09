package com.financify.data.data_sources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saving_goals")
data class SavingGoal(
    @PrimaryKey val id: String,
   val userId: String, // Foreign key to the User
    val goalName: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val goalType: String,
    val selectedDate: String,
    val note: String,
    val color: String,
val icon: String
)

