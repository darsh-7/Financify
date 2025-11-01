package com.financify.data.data_sources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saving_goals")
data class SavingGoal(
    @PrimaryKey val id: String,
    //val userId: String, // Foreign key to the User
    val name: String, // e.g., "iPhone 13 Mini", "Car"
    val targetAmount: Double,
    var currentAmount: Double = 0.0,
    val color: String, // Hex color code
//    val icon: String // An identifier for an icon (e.g., "ic_phone", "ic_car")
)