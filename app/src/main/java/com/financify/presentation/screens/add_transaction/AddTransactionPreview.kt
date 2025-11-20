package com.financify.presentation.screens.add_transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionPrev(
    navController: NavController,
    type: TransactionType
) {
    val coroutineScope = rememberCoroutineScope()
    val categories = emptyList<Transaction>()

    val categoryColors = listOf(
        Color(0xFF377CC8),
        Color(0xFFE0533D),
        Color(0xFFE78C9D),
        Color(0xFFFBC02D)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDFDFD))
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            if (categories.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val bgColor = categoryColors[index % categoryColors.size]
                        CategoryCard(
                            transaction = category,
                            backgroundColor = bgColor
                        ) {
                        }
                    }
                }
            } else {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .size(160.dp, 140.dp)
                        .padding(top = 12.dp),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No\nCategories Yet!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxSize().padding(bottom = 18.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFDFDFD)),
                ) {
                    TransactionTypeDropDown(
                        selectedType = TransactionType.INCOME,
                        onTypeSelected = { TransactionType.INCOME })

                    InputField(
                        label = "Amount",
                        value = "",
                        onValueChange = { "" },
                        keyboardType = KeyboardType.Number
                    )

                    InputField(
                        label = "Title (e.g., Shopping, Salary, Rent)",
                        value = "",
                        onValueChange = { "" },
                    )

                    InputField(
                        label = "Date",
                        value = "1/11/2025",
                        onValueChange = { "1/11/2025" },
                        trailingIcon = {
                            IconButton(onClick = { /* Handle date selection */ }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Date")
                            }
                        })

                    InputField(
                        label = "Description",
                        value = "",
                        onValueChange = { "" },
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = { true })
                        Text("Save as Category")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val transactionId = "viewModel.saveTransactionAndGetId()"
                                navController.navigate("receipt/$transactionId")
                            }
                        },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3629B6)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false, showSystemUi = false)
@Composable
private fun AddTransactionPreview() {
    AddTransactionPrev(navController = rememberNavController(), type = TransactionType.INCOME)
}