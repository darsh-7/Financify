package com.financify.presentation.components

import android.R.attr.text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financify.presentation.theme.Add_goal_savebtn


@Composable
fun SaveButton(isEditMode: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical =20.dp)
            .height(55.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Add_goal_savebtn),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = "Save",
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isEditMode) "Update Goal" else "Save Goal",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

    }
    Spacer(modifier = Modifier.height(20.dp))


}