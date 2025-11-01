package com.financify.presentation.navigation

import com.financify.presentation.screens.home_screen.IssuesListScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financify.presentation.screens.cam_scan_screen.RepoListScreen
import com.financify.presentation.screens.transaction_screen.RepoDetailsScreen
import com.financify.presentation.utils.Constants.Companion.NAME_ARGUMENT_KEY
import com.financify.presentation.utils.Constants.Companion.OWNER_ARGUMENT_KEY

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.RepoListScreen.route
    ) {
        composable(route = Screens.RepoListScreen.route) {
            RepoListScreen { ownerName, name ->
                navController.navigate(Screens.RepoDetailsScreen.passOwnerAndName(ownerName, name))
            }
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
    }
}