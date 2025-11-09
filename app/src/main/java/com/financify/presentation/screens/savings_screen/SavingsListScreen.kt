package com.financify.presentation.screens.savings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.financify.data.data_sources.local.room.AppDatabase
import com.financify.data.repository.SavingGoalRepository
import com.financify.presentation.screens.savings_screen.components.GoalListTopBar
import com.financify.presentation.screens.savings_screen.components.GoalsHeader
import com.financify.presentation.screens.savings_screen.components.SavingsListContent
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModelFactory

@Composable
fun SavingsListScreen(navController: NavController) {
 val context = LocalContext.current

 val database = remember { AppDatabase.getDatabase(context) }
 val repository = remember { SavingGoalRepository(database.savingGoalDao()) }
 val viewModel: SavingGoalViewModel = viewModel(
  factory = SavingGoalViewModelFactory(repository)
 )

 val goalsList by viewModel.allGoals.collectAsState()
 val stats by viewModel.totalStats.collectAsState()

 Scaffold(
  topBar = {
   GoalListTopBar(navController = navController)
  },
  content = { paddingValues ->
   Column(
    modifier = Modifier
     .fillMaxSize()
     .padding(paddingValues)
     .background(Color(0xFFF5F7FA))
   ) {
//   GoalsHeader(stats)

    SavingsListContent(
     navController = navController,
     viewModel = viewModel,
     goalsList = goalsList
    )
   }
  }
 )
}