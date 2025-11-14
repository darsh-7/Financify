package com.financify.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financify.presentation.theme.Add_goal_mainColor

@Composable
fun AddGoalHeader(isEditMode: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.TrackChanges,
            contentDescription = "Goal Icon",
            modifier = Modifier
                .size(64.dp)
//                .background(
//                    Color.White.copy(alpha = 0.7f)   ,
//                    shape = CircleShape
//                )
//                .padding(2.dp),
//            tint = MaterialTheme.colorScheme.primary,
//            ,tint = Color.White
            ,tint = Add_goal_mainColor

            )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isEditMode) "Edit Saving Goal" else "New Saving Goal",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (isEditMode) "Update your goal details" else "Define your goal and start your savings journey",
            color= Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center
        )
    }
}