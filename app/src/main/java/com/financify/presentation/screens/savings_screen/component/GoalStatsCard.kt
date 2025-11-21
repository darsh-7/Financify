package com.financify.presentation.screens.savings_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.screens.savings_screen.viewmodel.GoalStats
import java.text.DecimalFormat

val PrimaryBlue = Color(0xFF1A79E1)
val LighterBlue = PrimaryBlue.copy(alpha = 0.9f)
val TrackColor = PrimaryBlue.copy(alpha = 0.2f)
val StatsCardBackground = Color(0xFFFFFFFF)

@Composable
fun GoalsHeader(stats: GoalStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding( vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StatsCardBackground,contentColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.TrackChanges,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Saving Goals",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun TotalStatsCard(stats: GoalStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StatsCardBackground,contentColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Statistics",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem(label = "Total Saved", value = stats.totalSaved, modifier = Modifier.weight(1f))
                StatItem(label = "Total Target", value = stats.totalTarget, modifier = Modifier.weight(1f))
                StatItem(label = "Number of Goals", value = stats.totalGoals.toDouble(), isMoney = false, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            TotalProgressRow(
                progress = stats.totalProgress,
                barColor = LighterBlue
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: Double, isMoney: Boolean = true, modifier: Modifier = Modifier) {
    val formatter = remember { DecimalFormat("#,###.##") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray), maxLines = 1)
        Text(
            text = if (isMoney) "${formatter.format(value)} EGP" else formatter.format(value),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
    }
}

@Composable
fun TotalProgressRow(progress: Double, barColor: Color) {
    val progressFloat = (progress / 100).toFloat().coerceIn(0f, 1f)
    val decimalFormat = remember { DecimalFormat("#.##") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Total Progress", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { progressFloat },
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = barColor,
                trackColor = TrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${decimalFormat.format(progress)}%",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}