package com.financify.presentation.screens.savings_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.financify.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalListTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "Savings",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Screens.AddGoalScreen.passGoalId(null))
                }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add Goal",
                    tint = Color.Black
                )
            }
        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color(0xFFF5F7FA)
//        )
    )
}