package com.financify.presentation.screens.analysis_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financify.data.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AnalysisScreen(dataStoreManager: DataStoreManager) {
    val biometricEnabled by dataStoreManager.biometricEnabledFlow.collectAsState(initial = false)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Enable Biometric Authentication")
        Switch(
            checked = biometricEnabled ?: false,
            onCheckedChange = { isChecked ->
                CoroutineScope(Dispatchers.IO).launch {
                    dataStoreManager.setBiometricEnabled(isChecked)
                }
            }
        )
    }
}