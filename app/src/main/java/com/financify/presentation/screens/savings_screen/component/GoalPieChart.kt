package com.financify.presentation.screens.savings_screen.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun GoalPieChart(chartItems: List<ChartItemData>) {
    val pieChartData: List<Pie> = chartItems.map { item ->
        Pie(
            data = item.value.toDouble(),
            color = item.color,
            label = item.label,
        )
    }

    PieChart(
        modifier = Modifier.fillMaxSize(),
        data = pieChartData,
        spaceDegree = 3f,
        style = Pie.Style.Fill,
//        onPieClick = { pie ->
//        }
    )
}