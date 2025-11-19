package com.financify.presentation.screens.cam_scan_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.financify.presentation.screens.add_transaction.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    viewModel: TransactionViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val transactions = viewModel.transactions.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(transactions.itemCount) { transaction ->
                TransactionItem(transaction = transactions[transaction]!!)
            }
        }
    }
}
