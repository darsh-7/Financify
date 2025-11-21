package com.financify.presentation.screens.analysis_screen.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.financify.presentation.navigation.Screens

import com.financify.presentation.screens.savings_screen.component.GoalsCompletionStats
import com.financify.presentation.screens.savings_screen.components.TotalStatsCard
import com.financify.presentation.screens.savings_screen.viewmodel.GoalStats
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel

@Composable
fun SavingsAnalysisTab(
    navController: NavController,
    savingViewModel: SavingGoalViewModel
) {
    val totalStats by savingViewModel.totalStats.collectAsState()
    val hasData = totalStats.totalGoals > 0
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        if (hasData) {
            GoalsCompletionStats(viewModel = savingViewModel)
            Spacer(Modifier.height(8.dp))
            TotalStatsCard(stats = totalStats)
            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Screens.SavingListScreen.route)
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Savings List")
                Spacer(Modifier.width(8.dp))
                Text("View and Edit Savings Goals")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "No Data",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "No savings goals recorded yet.",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Start by adding a new goal to see your analysis here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }


    }
}
