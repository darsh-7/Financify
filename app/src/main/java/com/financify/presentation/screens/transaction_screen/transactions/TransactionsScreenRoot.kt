package com.financify.presentation.screens.transaction_screen.transactions

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.financify.data.data_sources.local.room.entities.Transaction
import com.financify.data.data_sources.local.room.entities.TransactionType
import kotlin.math.abs

@Composable
fun TransactionsScreenRoot(
    userId: String?,
    viewModel: TransactionsViewModel = viewModel(),
    onBackClick: () -> Unit,
) {
    val paginatedTransactions = viewModel.paginatedTransactions.collectAsLazyPagingItems()
    val selectedTransaction by viewModel.selectedTransaction.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    TransactionsScreen(
        paginatedTransactions = paginatedTransactions,
        selectedTransaction = selectedTransaction,
        selectedFilter = selectedFilter,
        onBackClick = onBackClick,
        onTransactionClick = { transaction ->
            viewModel.getTransactionById(transaction.id)
        },
        onDismissPopup = {
            viewModel.clearSelectedTransaction()
        },
        onFilterSelected = { filter ->
            viewModel.setFilter(filter)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    paginatedTransactions: LazyPagingItems<Transaction>,
    selectedTransaction: Transaction?,
    selectedFilter: TransactionFilter,
    onBackClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    onDismissPopup: () -> Unit,
    onFilterSelected: (TransactionFilter) -> Unit
) {
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
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Filters", modifier = Modifier.weight(1f), color = Color.Black)
                Text(selectedFilter.displayName, color = Color.Black, fontSize = 12.sp)
                FilterDropdown(selectedFilter, onFilterSelected)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (paginatedTransactions.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (paginatedTransactions.itemCount == 0 && paginatedTransactions.loadState.refresh is LoadState.NotLoading) {
                     EmptyTransactionsView()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = paginatedTransactions.itemCount,
                            key = { index -> 
                                val transaction = paginatedTransactions[index]
                                transaction?.id ?: index 
                            }
                        ) { index ->
                            paginatedTransactions[index]?.let { transaction ->
                                TransactionCell(
                                    transaction = transaction,
                                    onClick = { onTransactionClick(it) },
                                )
                            }
                        }
                        
                        when (val state = paginatedTransactions.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Text(
                                        text = "Error: ${state.error.localizedMessage}",
                                        color = Color.Red,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
                 
                if (paginatedTransactions.loadState.refresh is LoadState.Error) {
                    val e = paginatedTransactions.loadState.refresh as LoadState.Error
                    Text(text = "Error: ${e.error.localizedMessage}", modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
            }
        }

        selectedTransaction?.let {
            TransactionDetailsPopup(
                transaction = it,
                onDismiss = onDismissPopup
            )
        }
    }
}

@Composable
fun BoxScope.EmptyTransactionsView() {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "No Transactions",
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No transactions yet",
            fontSize = 18.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FilterDropdown(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit
) {
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
                tint = Color.DarkGray
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
                            fontWeight = if (filter == selectedFilter) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onFilterSelected(filter)
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
    transaction: Transaction,
    onClick: (Transaction) -> Unit,
) {
    val isIncome = transaction.type == TransactionType.INCOME
    val color = if (isIncome) Color(0xFF00C853) else Color(0xFFD50000)
    val valueText = (if (isIncome) "+ " else "- ") + "$${abs(transaction.amount)}"

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(shape = RoundedCornerShape(16.dp), color = Color.White)
            .clickable { onClick(transaction) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TransactionIcon(
            name = transaction.title,
            value = if (isIncome) 1 else -1,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = transaction.title,
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

@Composable
fun TransactionIcon(
    name: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    val firstLetter = name.firstOrNull()?.uppercase() ?: ""
    val isIncome = value > 0
    val incomeColor = Color(0xFF00C853)
    val expenseColor = Color(0xFFD50000)
    val neutralColor = Color.LightGray.copy(alpha = 0.5f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(48.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 4.dp.toPx()
            val inset = strokeWidth / 2
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(inset, inset)
            val gapAngle = 10f

            drawArc(
                color = if (isIncome) incomeColor else neutralColor,
                startAngle = 180f + gapAngle / 2,
                sweepAngle = 180f - gapAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = if (!isIncome) expenseColor else neutralColor,
                startAngle = 0f + gapAngle / 2,
                sweepAngle = 180f - gapAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = firstLetter,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}
