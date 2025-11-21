package com.financify.presentation.screens.analysis_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.model.MonthlyStats

@Composable
fun AnalysisMessageCard(
    netIncome: Double,
    stats: List<MonthlyStats>
) {
  
    val negativeMonths = stats
        .filter { it.expense > it.income }
        .map { it.monthName }
        .joinToString(", ")

    val analysisMessage = when {
        netIncome > 0 -> "Congratulations! You achieved net savings of $$netIncome this period."
        netIncome < 0 -> {
            if (negativeMonths.isNotEmpty()) {
                "Alert! Your expenses exceeded your income by $${-netIncome}. Review spending for: $negativeMonths."
            } else {
                "Alert! Your expenses exceeded your income by $${-netIncome}. Please review your spending."
            }
        }
        else -> "Balanced performance. Your expenses exactly equaled your income this period."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (netIncome > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    ) {
        Text(
            text = analysisMessage,
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp,
            color = if (netIncome > 0) Color(0xFF388E3C) else Color(0xFFD32F2F)
        )
    }
}