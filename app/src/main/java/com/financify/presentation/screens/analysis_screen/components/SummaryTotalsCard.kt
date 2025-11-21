package com.financify.presentation.screens.analysis_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryTotalsCard(
    totalIncome: Double,
    totalExpense: Double,
    netIncome: Double,
    analysisMainColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = analysisMainColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text("Summary", fontSize = 14.sp)

            Spacer(Modifier.height(10.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Income:", fontSize = 16.sp)
                Text("$totalIncome", fontSize = 16.sp, color = Color(0xFF2E7D32))
            }

            Spacer(Modifier.height(6.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Expense:", fontSize = 16.sp)
                Text("$totalExpense", fontSize = 16.sp, color = Color(0xFFC62828))
            }

            // Net Income
            Spacer(Modifier.height(10.dp))

            val netIncomeColor = if (netIncome >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)

            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                DividerDefaults.color
            )

            Spacer(Modifier.height(10.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Net Income (Savings):", fontSize = 16.sp, style = MaterialTheme.typography.titleMedium)
                Text(
                    "$netIncome",
                    fontSize = 16.sp,
                    color = netIncomeColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}