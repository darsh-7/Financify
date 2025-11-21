package com.financify.presentation.screens.savings_screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel
import com.financify.presentation.theme.Stats_card_background

@Composable
fun GoalsCompletionStats(viewModel: SavingGoalViewModel) {
    val stats = viewModel.completionStats.collectAsState().value
    val totalGoals = stats.totalGoals

    val chartItems = listOf(
        ChartItemData(
            label = "Completed On Time",
            value = stats.completedOnTime,
            color = Color(0xFF4CAF50)
        ),
        ChartItemData(
            label = "Completed Late",
            value = stats.completedLate,
            color = Color(0xFFFF9800)
        ),
        ChartItemData(
            label = "Past Due (Not Completed)",
            value = stats.notCompletedPastDue,
            color = Color(0xFFE53935)
        ),
        ChartItemData(
            label = "In Progress (Within Deadline)",
            value = stats.notCompletedWithinDeadline,
            color = Color(0xFF64B5F6)
        )
    ).filter { it.value > 0 }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Stats_card_background,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Goal Completion Stats (${totalGoals} goals)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(Modifier.height(16.dp))

            if (totalGoals == 0) {
                Text(
                    text = "There are no goals to display statistics for.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GoalPieChart(chartItems = chartItems)
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.5f))

                    Spacer(Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
//                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        chartItems.forEach { item ->
                            val percentage = (item.value.toDouble() / totalGoals * 100)
                            LegendItem(
                                color = item.color,
                                label = item.label,
                                valueText = "${item.value} (${String.format("%.1f", percentage)}%)"
                            )
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}