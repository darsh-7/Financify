package com.financify.presentation.screens.receipt_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.financify.presentation.navigation.AppNavHost
import com.financify.presentation.screens.add_transaction.ui.theme.FinancifyTheme

class ReceiptActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancifyTheme {
                AppNavHost()
            }
        }
    }
}