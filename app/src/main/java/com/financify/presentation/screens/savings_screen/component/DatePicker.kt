package com.financify.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.theme.Add_goal_mainColor
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerCard(
    selectedDate: Date?,
    onDateSelected: (Date) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.size(20.dp),
                    tint = Add_goal_mainColor
                )
                Text(text = "Target Date",  color = Add_goal_mainColor,
                    fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = selectedDate?.let { dateFormat.format(it) } ?: "",
                onValueChange = {},
                label = { Text("Select Date" ,color = Add_goal_mainColor,
                   fontSize = 14.sp) },
                placeholder = { Text("Tap to select the date") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Calendar icon",
                        tint = Add_goal_mainColor
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                enabled = false,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Add_goal_mainColor.copy(alpha = 0.5f),
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = Add_goal_mainColor,
                    disabledTrailingIconColor = Add_goal_mainColor
                )
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
//                colors = DatePickerDefaults.colors( containerColor = Add_goal_background,
//                    headlineContentColor = Color.White,
//                    weekdayContentColor = Color.White.copy(alpha = 0.7f),
//                    todayContentColor = Color.White,
//                    selectedDayContainerColor = Color.White,
//                    selectedDayContentColor = Add_goal_background,
//                    currentYearContentColor = Color.White,
//                    subheadContentColor = Color.White,
//                )
            )
        }
    }
}