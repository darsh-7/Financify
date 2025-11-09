package com.financify.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.screens.savings_screen.AllGoalTypes
import com.financify.presentation.screens.savings_screen.GoalType
import com.financify.presentation.screens.savings_screen.utils.getGoalColorByName
import com.financify.presentation.theme.Add_goal_mainColor

//import com.financify.presentation.theme.getGoalColorByType

@Composable
fun GoalTypeGridStyled(
    selectedGoal: GoalType,
    onGoalSelected: (GoalType) -> Unit
) {
    val goals = AllGoalTypes

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Select Goal Type",
                modifier = Modifier.size(20.dp),
                tint = Color.Green
            )
            Text(text = "Select Goal Type",   color = Add_goal_mainColor,
                fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .height(170.dp)
                .padding(8.dp),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(goals) { goal ->
                val goalColor = getGoalColorByName(goal.name)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = if (goal == selectedGoal) goalColor else goalColor.copy(alpha = 0.1f)
                        )
                        .clickable { onGoalSelected(goal) }
                        .padding(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = goal.icon,
                            contentDescription = goal.name,
                            tint = if (goal == selectedGoal) Color.White else goalColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = goal.name,
                            color = if (goal == selectedGoal) Color.White else Color.Black,
                            fontSize = 12.sp
                        )
                    }

                    if (goal == selectedGoal) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }
    }
}