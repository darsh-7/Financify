package com.financify.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.theme.Add_goal_mainColor

val LightGrayBackground = Color(0xFFF5F5F5)
val PrimaryBlue = Color(0xFF1E88E5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalInputFields(
    goalName: String,
    onGoalNameChange: (String) -> Unit,
    targetAmountText: String,
    onTargetAmountChange: (String) -> Unit,
    savedAmountText: String,
    onSavedAmountChange: (String) -> Unit
) {
    val MySemiBoldStyle = TextStyle(
        color = Add_goal_mainColor,
        fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = goalName,
                    onValueChange = onGoalNameChange,
                    label = { Text(text = "Goal Name" ,color = Add_goal_mainColor,
                         fontSize = 16.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Add_goal_mainColor,
                        unfocusedBorderColor = Add_goal_mainColor.copy(alpha = 0.5f)
                    )
                )
            }
        }

                Spacer (modifier = Modifier.height(8.dp))

                Card (
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
                    imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                    contentDescription = "Finance Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Green
                )
                Text(
                    text = "Finance Details",
                    style = MySemiBoldStyle
                )
            }

            OutlinedTextField(
                value = targetAmountText,
                onValueChange = { newValue ->
                    onTargetAmountChange(newValue.filter { it.isDigit() || it == '.' })
                },
                label = { Text(text = "Target Amount", color = Add_goal_mainColor, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor =Add_goal_mainColor,
                    unfocusedBorderColor = Add_goal_mainColor.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = savedAmountText,
                onValueChange = { newValue ->
                    onSavedAmountChange(newValue.filter { it.isDigit() || it == '.' })
                },
                label = { Text(text = "Saved Amount",color=Add_goal_mainColor, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Add_goal_mainColor,
                    unfocusedBorderColor = Add_goal_mainColor.copy(alpha = 0.5f)
                )
            )
        }
    }
}