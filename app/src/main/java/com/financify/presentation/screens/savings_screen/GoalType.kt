package com.financify.presentation.screens.savings_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class GoalType(val name: String, val icon: ImageVector) {
    object Home : GoalType("Home", Icons.Outlined.Home)
    object General : GoalType("General", Icons.Outlined.TrackChanges)
    object Car : GoalType("Car", Icons.Outlined.DirectionsCar)
    object Travel : GoalType("Travel", Icons.Outlined.Flight)
    object Education : GoalType("Education", Icons.Outlined.School)
    object Health : GoalType("Health", Icons.Outlined.Favorite)
    object Shopping : GoalType("Shopping", Icons.Outlined.ShoppingCart)
    object Project : GoalType("Project", Icons.Outlined.Work)
}

val AllGoalTypes = listOf(
    GoalType.General,
    GoalType.Home,
    GoalType.Car,
    GoalType.Travel,
    GoalType.Education,
    GoalType.Health,
    GoalType.Shopping,
    GoalType.Project
)