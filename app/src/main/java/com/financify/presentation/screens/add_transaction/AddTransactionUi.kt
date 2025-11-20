package com.financify.presentation.screens.add_transaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionUi(
    viewModel: TransactionViewModel,
    navController: NavController,
    type: TransactionType
) {
    val coroutineScope = rememberCoroutineScope()
    val categories by viewModel.categories.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    val isLoading = viewModel.isOcrLoading || viewModel.isParsing

    val categoryColors = listOf(
        Color(0xFF377CC8),
        Color(0xFFE0533D),
        Color(0xFFE78C9D),
        Color(0xFFFBC02D)
    )

    LaunchedEffect(Unit) {
        viewModel.type = type
    }

    if (showDatePicker) {
        DatePicker(onDateSelected = { millis ->
            viewModel.onDateSelected(millis)
            showDatePicker = false
        }, onDismiss = { showDatePicker = false })
    }

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
        if (isLoading) {
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
                                viewModel.fillFormWithTransaction(it)
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
                    modifier = Modifier.fillMaxSize().padding(bottom = 14.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFDFDFD)),
                    ) {
                        TransactionTypeDropDown(
                            selectedType = viewModel.type, onTypeSelected = { viewModel.type = it })

                        InputField(
                            label = "Amount",
                            value = viewModel.amount,
                            onValueChange = { viewModel.amount = it },
                            keyboardType = KeyboardType.Number
                        )

                        InputField(
                            label = "Title (e.g., Shopping, Salary, Rent)",
                            value = viewModel.title,
                            onValueChange = { viewModel.title = it },
                        )

                        InputField(
                            label = "Date",
                            value = viewModel.selectedDate?.let { viewModel.formatDate(it) }
                                ?: viewModel.formatDate(System.currentTimeMillis()),
                            onValueChange = {},
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = "Select Date"
                                    )
                                }
                            })

                        InputField(
                            label = "Description",
                            value = viewModel.description,
                            onValueChange = { viewModel.description = it },
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = viewModel.isCategory,
                                onCheckedChange = { viewModel.isCategory = it })
                            Text("Save as Category")
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val transactionId = viewModel.saveTransactionAndGetId()
                                    navController.navigate("receipt/$transactionId")
                                }
                            },
                            enabled = viewModel.amount.isNotEmpty(),
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCard(transaction: Transaction, backgroundColor: Color, onClick: (Transaction) -> Unit) {
    Card(
        modifier = Modifier
            .size(160.dp, 140.dp)
            .clickable { onClick(transaction) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor, contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = transaction.title.first().uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = backgroundColor
                    )
                }
                if (transaction.type == TransactionType.INCOME) Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = Color.Green
                )
                else Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                transaction.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
            )

            Text(
                fontSize = 18.sp,
                text = "${transaction.amount}$",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeDropDown(
    selectedType: TransactionType, onTypeSelected: (TransactionType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val types = listOf(TransactionType.INCOME, TransactionType.EXPENSE)

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        InputField(
            label = "Transaction Type",
            value = selectedType.name.lowercase().replaceFirstChar { it.uppercase() },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            types.forEach { type ->
                DropdownMenuItem(text = {
                    Text(
                        type.name.lowercase().replaceFirstChar { it.uppercase() },
                        fontSize = 16.sp
                    )
                }, onClick = {
                    onTypeSelected(type)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        label = { Text(label) },
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        singleLine = true,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(onDateSelected: (Long) -> Unit, onDismiss: () -> Unit) {

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            datePickerState.selectedDateMillis?.let { millis ->
                val dateCalendar = Calendar.getInstance().apply { timeInMillis = millis }
                val now = Calendar.getInstance()
                dateCalendar.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY))
                dateCalendar.set(Calendar.MINUTE, now.get(Calendar.MINUTE))
                dateCalendar.set(Calendar.SECOND, now.get(Calendar.SECOND))
                onDateSelected(dateCalendar.timeInMillis)
            }
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}