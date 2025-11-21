package com.financify.presentation.screens.analysis_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.* // استيراد شامل لمكونات Material 3 الجديدة
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.financify.presentation.screens.analysis_screen.viewmodel.AnalysisViewModel

@OptIn(ExperimentalMaterial3Api::class) // مطلوب لاستخدام ExposedDropdownMenuBox
@Composable
fun MonthsDropdown(
    viewModel: AnalysisViewModel,
    selectedMonthsText: String,
    onMonthsTextChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(1, 3, 6, 9, 12)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = selectedMonthsText,
            onValueChange = {},
            label = { Text("Duration") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { monthCount ->
                DropdownMenuItem(
                    text = { Text("$monthCount months") },
                    onClick = {
                        viewModel.setMonths(monthCount)
                        onMonthsTextChange("$monthCount months")
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}