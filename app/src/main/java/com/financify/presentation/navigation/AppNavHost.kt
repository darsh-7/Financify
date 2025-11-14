package com.financify.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financify.presentation.screens.analysis_screen.AnalysisScreen
import com.financify.presentation.screens.cam_scan_screen.RepoListScreen
import com.financify.presentation.screens.home_screen.IssuesListScreen
import com.financify.presentation.screens.home_screen.component.ui.HomeScreen
import com.financify.presentation.screens.text_recognition_screen.TextRecognitionResultScreen
import com.financify.presentation.screens.text_recognition_screen.TextRecognitionScreen
import com.financify.presentation.screens.transaction_screen.RepoDetailsScreen
import com.financify.presentation.utils.Constants
import java.net.URLDecoder

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.HomeScreen.route,
            modifier = Modifier.padding(it)
        ) {
            composable(route = Screens.HomeScreen.route) {
                HomeScreen()
            }
            composable(route = Screens.TextRecognitionScreen.route) {
                TextRecognitionScreen(navController = navController)
            }
            composable(route = Screens.AnalysisScreen.route) {
                AnalysisScreen()
            }

            composable(
                route = Screens.TextRecognitionResultScreen.route,
                arguments = listOf(navArgument(Constants.TEXT_ARGUMENT_KEY) { type = NavType.StringType })
            ) { navBackStackEntry ->
                val text = navBackStackEntry.arguments?.getString(Constants.TEXT_ARGUMENT_KEY)
                if (text != null) {
                    TextRecognitionResultScreen(text = URLDecoder.decode(text, "UTF-8"))
                }
            }

            composable(route = Screens.RepoListScreen.route) {
                RepoListScreen { ownerName, name ->
                    navController.navigate(Screens.RepoDetailsScreen.passOwnerAndName(ownerName, name))
                }
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
        }
    }
}
