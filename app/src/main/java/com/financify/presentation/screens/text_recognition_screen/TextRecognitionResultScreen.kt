package com.financify.presentation.screens.text_recognition_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.presentation.screens.text_recognition_screen.viewmodel.TextRecognitionViewModel

@Composable
fun TextRecognitionResultScreen(text: String, viewModel: TextRecognitionViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf<TransactionType?>(null) }

    LaunchedEffect(Unit) {
        viewModel.analyzeReceipt(text)
    }

    LaunchedEffect(state.parsedTransaction) {
        state.parsedTransaction?.let {
            title = it.title ?: ""
            amount = it.amount?.toString() ?: ""
            category = it.category ?: ""
            date = it.date ?: ""
            description = it.description ?: ""
        }
    }

    if (state.isParsing) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Analyzing Receipt...")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Receipt Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { transactionType = TransactionType.INCOME }) {
                    Text("Income")
                }
                Button(onClick = { transactionType = TransactionType.EXPENSE }) {
                    Text("Expense")
                }
            }
            transactionType?.let {
                Text("Selected: $it", modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally))
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { /* TODO: Save transaction */ }, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))) {
                Text("Save Transaction", color = Color.White)
            }
        }
    }
}