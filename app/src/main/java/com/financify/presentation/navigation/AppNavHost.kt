package com.financify.presentation.navigation

import androidx.biometric.BiometricManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financify.data.DataStoreManager
import com.financify.data.data_sources.local.room.AppDatabase
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.SavingGoalRepository
import com.financify.data.repository.TransactionRepository
import com.financify.presentation.screens.add_transaction.AddTransactionUi
import com.financify.presentation.screens.add_transaction.TransactionViewModel
import com.financify.presentation.screens.add_transaction.TransactionViewModelFactory
import com.financify.presentation.screens.analysis_screen.AnalysisScreen
import com.financify.presentation.screens.analysis_screen.viewmodel.AnalysisViewModel
import com.financify.presentation.screens.analysis_screen.viewmodel.AnalysisViewModelFactory
import com.financify.presentation.screens.cam_scan_screen.TransactionListScreen
import com.financify.presentation.screens.home_screen.IssuesListScreen
import com.financify.presentation.screens.home_screen.component.ui.HomeScreen
import com.financify.presentation.screens.receipt_screen.ReceiptUi
import com.financify.presentation.screens.savings_screen.AddGoalScreen
import com.financify.presentation.screens.savings_screen.SavingsListScreen
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModel
import com.financify.presentation.screens.savings_screen.viewmodel.SavingGoalViewModelFactory
import com.financify.presentation.screens.text_recognition_screen.TextRecognitionScreen
import com.financify.presentation.screens.transaction_screen.RepoDetailsScreen
import com.financify.presentation.utils.Constants
import java.net.URLDecoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val biometricEnabled by dataStoreManager.biometricEnabledFlow.collectAsState(initial = null)
    var showBiometricDialog by remember { mutableStateOf(false) }

    val dao = AppDatabase.getDatabase(context).TransactionDao()
    val repository = TransactionRepository(dao)
    val factory = TransactionViewModelFactory(repository)
    val viewModel: TransactionViewModel = viewModel(factory = factory)
    val savingGoalDao = AppDatabase.getDatabase(context).savingGoalDao()
    val savingGoalRepository = SavingGoalRepository(savingGoalDao)

    LaunchedEffect(biometricEnabled) {
        if (biometricEnabled == true) {
            val biometricManager = BiometricManager.from(context)
            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
                showBiometricDialog = false
            }
        } else if (biometricEnabled == null) {
            showBiometricDialog = true
        }
    }

    if (showBiometricDialog) {
        AlertDialog(
            onDismissRequest = { showBiometricDialog = false },
            title = { Text("Enable Biometric Authentication") },
            text = { Text("Do you want to use biometric authentication to secure your app?") },
            confirmButton = {
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStoreManager.setBiometricEnabled(true)
                    }
                    showBiometricDialog = false
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStoreManager.setBiometricEnabled(false)
                    }
                    showBiometricDialog = false
                }) {
                    Text("Disable")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screens.TextRecognitionScreen.route //&&
               // currentRoute != Screens.SavingListScreen.route
            ) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.HomeScreen.route,
            modifier = Modifier.padding(it)
        ) {
            composable(route = Screens.HomeScreen.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onAddTransactionClicked = { type ->
                        navController.navigate("transaction/${type.name}")
                    },
                    navController = navController
                )
            }
            composable(route = Screens.TextRecognitionScreen.route) {
                TextRecognitionScreen(viewModel = viewModel, navController = navController)
            }
            composable(route = Screens.AnalysisScreen.route) {

                val viewModel: AnalysisViewModel = viewModel(
                    factory = AnalysisViewModelFactory(repository)
                )

                val savingGoalViewModel: SavingGoalViewModel = viewModel(
                    factory = SavingGoalViewModelFactory(savingGoalRepository)
                )

                AnalysisScreen(
                    viewModel = viewModel,
                    savingGoalViewModel = savingGoalViewModel,
                    navController,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Screens.TransactionListScreen.route) {
                TransactionListScreen(viewModel, navController)
            }

            composable(route = Screens.SavingListScreen.route) { SavingsListScreen(navController = navController) }

            composable(
                route = Screens.AddGoalScreen.route,
                arguments = listOf(
                    navArgument("goalId") {
                        type = NavType.StringType
                        defaultValue = "-1"
                        nullable = true
                    }
                )
            ) {
                AddGoalScreen(navController = navController)
            }
            composable(route = Screens.AnalysisScreen.route) {

                val viewModel: AnalysisViewModel = viewModel(
                    factory = AnalysisViewModelFactory(repository)
                )

                val savingGoalViewModel: SavingGoalViewModel = viewModel(
                    factory = SavingGoalViewModelFactory(savingGoalRepository)
                )

                AnalysisScreen(
                    viewModel = viewModel,
                    savingGoalViewModel = savingGoalViewModel,
                    navController,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screens.RepoDetailsScreen.route,
                arguments = listOf(
                    navArgument(Constants.OWNER_ARGUMENT_KEY) {
                        type = NavType.StringType
                    },
                    navArgument(Constants.NAME_ARGUMENT_KEY) {
                        type = NavType.StringType
                    }
                )
            ) { navBackStackEntry ->
                val owner = navBackStackEntry.arguments?.getString(Constants.OWNER_ARGUMENT_KEY)
                val name = navBackStackEntry.arguments?.getString(Constants.NAME_ARGUMENT_KEY)
                if (owner != null && name != null) {
                    RepoDetailsScreen(
                        owner = owner,
                        name = name,
                        onShowIssuesClicked = {
                            navController.navigate(
                                Screens.IssuesListScreen.passOwnerAndNameIssue(
                                    owner,
                                    name
                                )
                            )
                        },
                        onClickBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(
                route = Screens.IssuesListScreen.route,
                arguments = listOf(
                    navArgument(Constants.OWNER_ARGUMENT_KEY) {
                        type = NavType.StringType
                    },
                    navArgument(Constants.NAME_ARGUMENT_KEY) {
                        type = NavType.StringType
                    }
                )
            ) { navBackStackEntry ->
                val owner = navBackStackEntry.arguments?.getString(Constants.OWNER_ARGUMENT_KEY)
                val name = navBackStackEntry.arguments?.getString(Constants.NAME_ARGUMENT_KEY)
                if (owner != null && name != null) {
                    IssuesListScreen(
                        owner = owner,
                        name = name,
                        onClickBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
            // Add Transaction Screen
            composable(
                route = "transaction/{type}",
                arguments = listOf(navArgument("type") {
                    type = NavType.StringType
                    defaultValue = TransactionType.INCOME.name
                })
            ) { navBackStackEntry ->
                val typeArg =
                    navBackStackEntry.arguments?.getString("type") ?: TransactionType.INCOME.name
                val type = TransactionType.valueOf(typeArg)
                AddTransactionUi(viewModel, navController, type)
            }
            // Receipt Screen
            composable(
                route = "receipt/{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) { navBackStackEntry ->
                val transactionId = navBackStackEntry.arguments?.getString("transactionId") ?: ""
                ReceiptUi(viewModel, transactionId, navController)
            }
        }
    }
}