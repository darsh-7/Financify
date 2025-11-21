package com.financify.presentation.screens.analysis_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.navigation.NavController
import com.financify.presentation.screens.analysis_screen.component.SavingsAnalysisTab
import com.financify.presentation.screens.analysis_screen.components.AnalysisHeader
import com.financify.presentation.screens.analysis_screen.components.IncomeExpenseTab
import com.financify.presentation.screens.analysis_screen.components.SegmentedTab
import com.financify.presentation.screens.analysis_screen.viewmodel.AnalysisViewModel
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel,
    savingGoalViewModel: SavingGoalViewModel,
    navController: NavController,
    onBack: () -> Unit = {}
) {
    val stats by viewModel.monthlyStats.collectAsState()
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Income vs Expense", "Savings Analysis")
    var selectedMonthsText by remember { mutableStateOf("Select months") }

    LaunchedEffect(stats) {
        Log.d("AnalysisData", stats.toString())
    }

    Scaffold(
        topBar = {
            AnalysisHeader(onBack = onBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(10.dp))

            SegmentedTab(
                tabTitles = tabTitles,
                selectedTab = selectedTab,
                onTabSelected = { index -> selectedTab = index }
            )

            Spacer(Modifier.height(20.dp))

            when (selectedTab) {
                0 -> IncomeExpenseTab(
                    stats = stats,
                    viewModel = viewModel,
                    selectedMonthsText = selectedMonthsText,
                    navController = navController,
                    onMonthsTextChange = { selectedMonthsText = it }
                )

                1 -> SavingsAnalysisTab(savingViewModel = savingGoalViewModel, navController = navController)
            }
        }
    }
}