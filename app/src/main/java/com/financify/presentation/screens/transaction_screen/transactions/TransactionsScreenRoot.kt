package com.financify.presentation.screens.transaction_screen.transactions

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit
import kotlin.math.abs


@Composable
fun TransactionsScreenRoot(
    userId: String?,
    onBackClick: () -> Unit,
) {
    TransactionsScreen(userId, onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(userId: String?, onBackClick: () -> Unit) {
    val selectedFilter = remember { mutableStateOf<TransactionFilter>(TransactionFilter.All) }
    var selectedTransaction by remember { mutableStateOf<TransactionData?>(null) }

    val allTransactions = remember {
        (1..20).map { i ->
            TransactionData(
                name = "Transaction $i",
                description = "Description for transaction $i",
                value = -10 + i * 2,
                timestamp = System.currentTimeMillis() - TimeUnit.DAYS.toMillis((20 - i).toLong())
            )
        }
    }

    val filteredTransactions = remember(selectedFilter.value, allTransactions) {
        when (selectedFilter.value) {
            is TransactionFilter.Income -> allTransactions.filter { it.value > 0 }
            is TransactionFilter.Expenses -> allTransactions.filter { it.value < 0 }
            is TransactionFilter.Recent -> allTransactions.sortedByDescending { it.timestamp }
            is TransactionFilter.Oldest -> allTransactions.sortedBy { it.timestamp }
            is TransactionFilter.All -> allTransactions
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Transactions") }, navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "BackButton"
                )
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = Color.LightGray.copy(alpha = 0.2f))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Filters", modifier = Modifier.weight(1f))
                Text(selectedFilter.value.displayName)
                FilterDropdown(selectedFilter)
            }

            filteredTransactions.forEach { transaction ->
                TransactionCell(
                    transaction = transaction,
                    onClick = { selectedTransaction = it },
                )
            }
        }

        selectedTransaction?.let {
            TransactionDetailsPopup(
                transaction = it,
                onDismiss = { selectedTransaction = null }
            )
        }
    }
}

@Composable
fun FilterDropdown(selectedFilter: MutableState<TransactionFilter>) {
    var expanded by remember { mutableStateOf(false) }

    val filterOptions = listOf(
        TransactionFilter.All,
        TransactionFilter.Income,
        TransactionFilter.Expenses,
        TransactionFilter.Recent,
        TransactionFilter.Oldest
    )

    Box {
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = Icons.Default.Menu,
                contentDescription = "Filters",
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filterOptions.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = filter.displayName,
                            fontWeight = if (filter == selectedFilter.value) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        selectedFilter.value = filter
                        expanded = false
                        Log.d("filter", "Filter selected: ${filter.displayName}")
                    }
                )
            }
        }
    }
}

@Composable
fun TransactionCell(
    transaction: TransactionData,
    onClick: (TransactionData) -> Unit,
) {
    val isIncome = transaction.value > 0
    val icon = if (isIncome) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val color = if (isIncome) Color(0xFF00C853) else Color(0xFFD50000)
    val valueText = (if (isIncome) "+ " else "- ") + "$${abs(transaction.value)}"

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(shape = RoundedCornerShape(16.dp), color = Color.White)
            .clickable { onClick(transaction) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isIncome) "Income" else "Expense",
            tint = color,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = transaction.name,
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = transaction.description,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Text(
            text = valueText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    }
}

@Preview
@Composable
fun TransactionsScreenPreview() {
    TransactionsScreen("userId") {}
}
