package com.financify.presentation.screens.analysis_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financify.presentation.model.MonthlyStats
import com.financify.presentation.screens.analysis_screen.utils.toBarData
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties

@Composable
fun IncomeExpenseChartCard(
    stats: List<MonthlyStats>,
    analysisMainColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = analysisMainColor)
    ) {
        if (stats.isNotEmpty()) {
            ColumnChart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                data = stats.toBarData(),
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.None,
                    spacing = 1.dp,
                    thickness = 10.dp
                ),
                gridProperties = GridProperties(false),
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No data for this period", color = Color.Gray)
            }
        }
    }
}