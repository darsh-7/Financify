package com.financify.presentation.screens.savings_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.presentation.screens.savings_screen.utils.calculateRemainingDaysText
import com.financify.presentation.screens.savings_screen.utils.getGoalColorByName
import com.financify.presentation.screens.savings_screen.utils.getIconImageVector
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
private fun formatTimestampToDateString(timestamp: Long): String {
    return remember(timestamp) {
        if (timestamp == 0L) {
            "N/A"
        } else {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
        }
    }
}
@Composable
fun SavingGoalCard(
    goal: SavingGoal,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val progress = if (goal.targetAmount > 0) (goal.savedAmount / goal.targetAmount) * 100 else 0.0

    val remainingDaysText = calculateRemainingDaysText(goal.selectedDate)
    val goalColor = getGoalColorByName(goal.color).copy(alpha = 0.7f)
    val formattedDate = formatTimestampToDateString(goal.selectedDate)
//    val progress = if (goal.targetAmount > 0) (goal.savedAmount / goal.targetAmount) * 100 else 0.0
//    val remainingDaysText = calculateRemainingDaysText(goal.selectedDate)
//    val goalColor = getGoalColorByName(goal.color).copy(alpha = 0.7f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = goalColor)  //.copy(alpha = 0.7f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getIconImageVector(goal.icon),
                        contentDescription = goal.goalName,
                        tint = Color.Black.copy(alpha = .8f),
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = goal.goalName,
                        color = Color.Black,
                        // fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black.copy(alpha = 0.7f).copy(alpha = 0.7f)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GoalAmountItem(
                    label = "Amount Saved",
                    amount = goal.savedAmount,
                    color = Color.Black.copy(alpha = 0.7f)
                )
                GoalAmountItem(
                    label = "Target",
                    amount = goal.targetAmount,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

//            GoalProgressRow(progress = progress, barColor = Color.Black.copy(alpha = 0.7f))
            GoalProgressRow(progress = progress, barColor = Color.Black.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Color.Black.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Target Date: $formattedDate",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }

                DateTag(remainingDaysText)
            }
        }
    }
}

@Composable
fun GoalAmountItem(label: String, amount: Double, color: Color) {
    val formatter = remember { DecimalFormat("#,###.##") }
    Column(
        modifier = Modifier.width(IntrinsicSize.Max),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Black.copy(alpha = 0.7f)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "${formatter.format(amount)} EGP",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            color = color
        )
    }
}

@Composable
fun DateTag(text: String) {
    val isExpired = text.contains("Expired")
    val bgColor = if (isExpired) Color(0xFFFFCCBC) else Color(0xFFC8E6C9)
    val textColor = if (isExpired) Color(0xFFE53935) else Color(0xFF388E3C)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun GoalProgressRow(progress: Double, barColor: Color) {
    val progressFloat = (progress / 100).toFloat().coerceIn(0f, 1f)
    val decimalFormat = remember { DecimalFormat("#.##") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Progress",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Black.copy(alpha = 0.7f))
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { progressFloat },
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = barColor,
                trackColor = barColor.copy(alpha = 0.2f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${decimalFormat.format(progress)}%",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}
//      #########################