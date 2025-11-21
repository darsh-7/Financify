package com.financify.presentation.screens.analysis_screen.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.financify.presentation.model.MonthlyStats
import ir.ehsannarmani.compose_charts.models.Bars

fun List<MonthlyStats>.toBarData(): List<Bars> {
    return this.map { item ->
        Bars(
            label = item.monthName.take(3)
            ,
            values = listOf(
                Bars.Data(
                    label = "Income",
                    value = item.income,
                    color = SolidColor(Color.Blue),
                ),
                Bars.Data(
                    label = "Expense",
                    value = item.expense,
                    color = SolidColor(Color.Red)
                )
            )
        )
    }
}