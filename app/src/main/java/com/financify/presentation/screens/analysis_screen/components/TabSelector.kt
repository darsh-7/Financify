package com.financify.presentation.screens.analysis_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.theme.analysis_noneSelectedTab
import com.financify.presentation.theme.analysis_selectedTap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedTab(
    tabTitles: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            Card(
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp, topEnd = 0.dp, bottomEnd = 0.dp)
                    else -> RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 25.dp, bottomEnd = 25.dp)
                },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(analysis_selectedTap.value) else Color(analysis_noneSelectedTab.value),
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}