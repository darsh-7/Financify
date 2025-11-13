package com.financify.presentation.navigation

import com.financify.presentation.screens.add_transaction.TransactionViewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financify.data.data_sources.local.room.AppDatabase
import com.financify.data.data_sources.local.room.entities.TransactionType
import com.financify.data.repository.TransactionRepository
import com.financify.presentation.screens.add_transaction.AddTransactionUi
import com.financify.presentation.screens.add_transaction.TransactionViewModelFactory
import com.financify.presentation.screens.cam_scan_screen.TransactionListScreen
import com.financify.presentation.screens.home_screen.IssuesListScreen
import com.financify.presentation.screens.receipt_screen.ReceiptUi
import com.financify.presentation.screens.transaction_screen.RepoDetailsScreen
import com.financify.presentation.utils.Constants.Companion.NAME_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.OWNER_ARGUMENT_KEY

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).TransactionDao()
    val repository = TransactionRepository(dao)
    val factory = TransactionViewModelFactory(repository)
    val viewModel: TransactionViewModel = viewModel(factory = factory)

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.RepoListScreen.route
    ) {
        composable(route = Screens.RepoListScreen.route) {
            /*
                            ---- TO BE REPLACED -----
            I found the RepoListScreen set as the start destination,
            so i treated it as the home screen just to test the app flow
             */
            TransactionListScreen(
                onAddTransactionClicked = { type ->
                    navController.navigate("transaction/${type.name}")
                }
            )
//            RepoListScreen { ownerName, name ->
//                navController.navigate(Screens.RepoDetailsScreen.passOwnerAndName(ownerName, name))
//            }
        }

        composable(
            route = Screens.RepoDetailsScreen.route,
            arguments = listOf(
                navArgument(OWNER_ARGUMENT_KEY){
                    type = NavType.StringType
                },
                navArgument(NAME_ARGUMENT_KEY){
                    type = NavType.StringType
                }
            )
        ){ navBackStackEntry ->
            val owner = navBackStackEntry.arguments?.getString(OWNER_ARGUMENT_KEY)
            val name = navBackStackEntry.arguments?.getString(NAME_ARGUMENT_KEY)
            if (owner!= null && name != null){
                RepoDetailsScreen(
                    owner = owner,
                    name = name,
                    onShowIssuesClicked = {
                        navController.navigate(Screens.IssuesListScreen.passOwnerAndNameIssue(owner, name))
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
                navArgument(OWNER_ARGUMENT_KEY){
                    type = NavType.StringType
                },
                navArgument(NAME_ARGUMENT_KEY){
                    type = NavType.StringType
                }
            )
        ){ navBackStackEntry ->
            val owner = navBackStackEntry.arguments?.getString(OWNER_ARGUMENT_KEY)
            val name = navBackStackEntry.arguments?.getString(NAME_ARGUMENT_KEY)
            if (owner!= null && name != null){
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