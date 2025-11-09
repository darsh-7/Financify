package com.financify.presentation.screens.savings_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.financify.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.financify.data.data_sources.local.room.entities.SavingGoal
import com.financify.presentation.navigation.Screens
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel

@Composable
fun SavingsListContent(
    navController: NavController,
    viewModel: SavingGoalViewModel,
    goalsList: List<SavingGoal>
) {
    var goalToDelete by remember { mutableStateOf<SavingGoal?>(null) }

    val showDeleteDialog = goalToDelete != null

    Column(modifier = Modifier.fillMaxSize()) {
        if (goalsList.isEmpty()) {
            EmptyGoalsMessage(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(goalsList, key = { it.id }) { goal ->
                    SavingGoalCard(
                        goal = goal,
//                    onDelete = { viewModel.deleteGoal(goal.id) },
                        onDelete = { goalToDelete = goal },

                        onEdit = {
                            navController.navigate(Screens.AddGoalScreen.passGoalId(goal.id))
                        }
                    )
                }
            }
        }
    }
    if (showDeleteDialog && goalToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                goalToDelete = null
            },
            icon = {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = "Delete Warning Icon",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(text = "Confirm Deletion", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            },
            text = {
                Text("Are you sure you want to permanently delete the goal '${goalToDelete!!.goalName}'?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteGoal(goalToDelete!!.id)
                        goalToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        goalToDelete = null
                    }
                ) {
                    Text("Cancel",color=Color.Gray)
                }
            }
        )
    }}

@Composable
fun EmptyGoalsMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.drawable.s1), contentDescription = "", Modifier.alpha(0.8f))
//        Icon(Icons.Filled.MoneyOff, contentDescription = null, tint = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.size(80.dp))
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Start Your Savings Journey!",
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Gray)//.copy(alpha = 0.7f))
        )
        Text(
            text = "Tap the Add button to create your first goal.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray.copy(alpha = 0.7f))
        )
    }
}

