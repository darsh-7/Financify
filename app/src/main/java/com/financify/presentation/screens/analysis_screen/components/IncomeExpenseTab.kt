package com.financify.presentation.screens.analysis_screen.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.financify.presentation.model.MonthlyStats
import com.financify.presentation.navigation.Screens
import com.financify.presentation.screens.analysis_screen.utils.toBarData
import com.financify.presentation.screens.analysis_screen.viewmodel.AnalysisViewModel
import com.financify.presentation.theme.analysis_mainColor

@Composable
fun IncomeExpenseTab(
    stats: List<MonthlyStats>,
    viewModel: AnalysisViewModel,
    selectedMonthsText: String,
    navController: NavController,
    onMonthsTextChange: (String) -> Unit
) {
    val totalIncome = stats.sumOf { it.income }
    val totalExpense = stats.sumOf { it.expense }
    val netIncome = totalIncome - totalExpense

    val hasData = stats.isNotEmpty()

    if (hasData) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MonthsDropdown(
                viewModel = viewModel,
                selectedMonthsText = selectedMonthsText,
                onMonthsTextChange = onMonthsTextChange
            )

            Spacer(Modifier.height(20.dp))

            IncomeExpenseChartCard(
                stats = stats,
                analysisMainColor = analysis_mainColor
            )

            Spacer(Modifier.height(25.dp))

            SummaryTotalsCard(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                netIncome = netIncome,
                analysisMainColor = analysis_mainColor
            )

            Spacer(Modifier.height(20.dp))

            AnalysisMessageCard(
                netIncome = netIncome,
                stats = stats
            )
            Spacer(Modifier.height(24.dp))
            OutlinedButton(
                onClick = {
                    navController.navigate(Screens.TransactionListScreen.route)
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Savings List")
                Spacer(Modifier.width(8.dp))
                Text("Review transactions")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "No transaction data",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No transactions recorded yet.",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Add income or expense transactions to view the analysis.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }

}