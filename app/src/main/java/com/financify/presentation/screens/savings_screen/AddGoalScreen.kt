package com.financify.presentation.screens.savings_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.financify.data.data_sources.local.room.AppDatabase
import com.financify.data.repository.SavingGoalRepository
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModelFactory
import com.financify.presentation.components.AddGoalHeader
import com.financify.presentation.components.GoalTypeGridStyled
import com.financify.presentation.components.GoalInputFields
import com.financify.presentation.components.DatePickerCard
import com.financify.presentation.components.NotesCard
import com.financify.presentation.components.SaveButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddGoalScreen(navController: NavController) {

    var selectedGoal by remember { mutableStateOf<GoalType?>(null) }
    var goalName by remember { mutableStateOf("") }
    var targetAmountText by remember { mutableStateOf("") }
    var savedAmountText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var noteText by remember { mutableStateOf("") }

    val backStackEntry = navController.currentBackStackEntry
    val goalId = backStackEntry?.arguments?.getString("goalId")
    val isEditMode = !goalId.isNullOrBlank() && goalId != "-1"

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val repository = SavingGoalRepository(database.savingGoalDao())

    val viewModel: SavingGoalViewModel = viewModel(
        factory = SavingGoalViewModelFactory(repository, goalId)
    )
    val currentGoal by viewModel.currentGoal.collectAsState()

    LaunchedEffect(currentGoal) {
        val existingGoal = currentGoal
        if (existingGoal != null) {
            goalName = existingGoal.goalName
            targetAmountText = existingGoal.targetAmount.toString().removeSuffix(".0")
            savedAmountText = existingGoal.savedAmount.toString().removeSuffix(".0")
            noteText = existingGoal.note

            selectedGoal =
                AllGoalTypes.firstOrNull { it.name == existingGoal.goalType } ?: GoalType.General

            try {
                selectedDate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(existingGoal.selectedDate)
            } catch (e: Exception) {
                selectedDate = null
            }
        }
    }
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(R.drawable.ic_launcher_background),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop,
//            alpha = 0.5f
//        )
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(color = Color.Transparent)
//                .background(Add_goal_background.copy(alpha = 0.7f))
                .background(Color(0xFFF5F7FA))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            AddGoalHeader(isEditMode = isEditMode)

            GoalTypeGridStyled(
                selectedGoal = selectedGoal ?: GoalType.General,
                onGoalSelected = { selectedGoal = it }
            )

            GoalInputFields(
                goalName = goalName,
                onGoalNameChange = { goalName = it },
                targetAmountText = targetAmountText,
                onTargetAmountChange = { targetAmountText = it },
                savedAmountText = savedAmountText,
                onSavedAmountChange = { savedAmountText = it }
            )

            DatePickerCard(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            NotesCard(
                noteText = noteText,
                onNoteChange = { noteText = it }
            )

            SaveButton(
                isEditMode = isEditMode
            ) {
                val validTargetAmount = targetAmountText.toDoubleOrNull() ?: 0.0
                val validSavedAmount = savedAmountText.toDoubleOrNull() ?: 0.0

                val goalToSave = selectedGoal ?: GoalType.General

                if (goalName.isNotEmpty() && validTargetAmount > 0.0) {

                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.saveGoal(
                            userId = "USER_ID_HERE",
                            goalName = goalName,
                            targetAmount = validTargetAmount,
                            savedAmount = validSavedAmount,
                            goalType = (selectedGoal ?: GoalType.General).name,
                            selectedDate = selectedDate?.let {
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.ENGLISH
                                ).format(it)
                            } ?: "",
                            note = noteText,
                            color = goalToSave.name,
                            icon = goalToSave.name,
                        )
                    }

                    Toast.makeText(
                        context,
                        if (isEditMode) "Goal updated successfully!" else "Goal saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "Please fill in the Goal Name and Target Amount",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            Spacer(modifier = Modifier.height(50.dp))

        }
    }
//}