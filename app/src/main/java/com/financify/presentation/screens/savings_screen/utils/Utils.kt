package com.financify.presentation.screens.savings_screen.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.financify.presentation.theme.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun calculateRemainingDaysText(targetDateMillis: Long): String {
    if (targetDateMillis == 0L) {
        return "No Date Set"
    }
    return try {
        val targetInstant = Instant.ofEpochMilli(targetDateMillis)

        val targetDate = targetInstant.atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now(ZoneId.systemDefault())

        val days = ChronoUnit.DAYS.between(today, targetDate)

        when {
            days > 0 -> {
                return when {
                    days <= 7 -> "$days days left"
                    days < 30 -> "${days / 7} weeks left"
                    else -> "$days days left"
                }
            }
            days == 0L -> "Due Date!"
            else -> "Expired"
        }
    } catch (e: Exception) {
        "Date Error"
    }

}

@Composable
fun getIconImageVector(iconName: String): ImageVector {
    return when (iconName) {
        "Home" -> Icons.Filled.Home
        "Car" -> Icons.Filled.DirectionsCar
        "Travel" -> Icons.Filled.Flight
        "Education" -> Icons.Filled.School
        "Health" -> Icons.Filled.Favorite
        "Shopping" -> Icons.Filled.ShoppingCart
        "Project" -> Icons.Filled.Work
        else -> Icons.Filled.Star
    }
}

@Composable
fun getGoalColorByName(goalName: String): Color {
    return when (goalName) {
        "Home" -> GoalColor_Home
        "Car" -> GoalColor_Car
        "Travel" -> GoalColor_Travel
        "Education" -> GoalColor_Education
        "Health" -> GoalColor_Health
        "Shopping" -> GoalColor_Shopping
        "Project" -> GoalColor_Project
        else -> GoalColor_General
    }
}