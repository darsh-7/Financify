package com.financify.presentation.screens.cam_scan_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import com.financify.R
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.presentation.screens.add_transaction.TransactionViewModel
import de.charlex.compose.SpeedDialData
import de.charlex.compose.SpeedDialFloatingActionButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TransactionListScreen(
    viewModel: TransactionViewModel,
    onAddTransactionClicked: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions = viewModel.transactions.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text("Financify Home") })
        },
        floatingActionButton = {
            SpeedDialFloatingActionButton(
                modifier = Modifier,
                initialExpanded = false,
                animationDuration = 300,
                animationDelayPerSelection = 100,
                showLabels = true,
                speedDialData = listOf(
                    SpeedDialData(
                        label = "Add EXPENSE",
                        painter = painterResource(id = R.drawable.keyboard_double_arrow_down)
                    ) {
                        onAddTransactionClicked(TransactionType.EXPENSE)
                    },
                    SpeedDialData(
                        label = "Add INCOME",
                        painter = painterResource(id = R.drawable.keyboard_double_arrow_up)
                    ) {
                        onAddTransactionClicked(TransactionType.INCOME)
                    },
                ),
            )
        }) { paddingValues ->
//        LazyColumn(modifier = Modifier.padding(paddingValues)) {
//            items(transactions) { transaction ->
//                if (transaction != null) {
//                    TransactionItem(transaction = transaction)
//                }
//            }
//        }
    }
}
