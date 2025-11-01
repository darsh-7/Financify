package com.financify.presentation.screens.savings_screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun RepoListScreen(
    onRepoItemClicked: (String, String) -> Unit
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListContent(

    onRefreshButtonClicked: () -> Unit,
    onRepoItemClicked: (String, String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(

    ) { innerPadding ->
//        when {
//            repoListUiSate.isLoading -> {
//                AnimateShimmerRepoList(
//                    innerPadding = innerPadding
//                )
//            }
//
//            repoListUiSate.isError -> {
//                ErrorSection(
//                    innerPadding = innerPadding,
//                    customErrorExceptionUiModel = repoListUiSate.customRemoteExceptionUiModel,
//                    onRefreshButtonClicked = {
//                        onRefreshButtonClicked()
//                    }
//                )
//            }
//
//
//
//            repoListUiSate.repoList.isNotEmpty() -> {
//                if (repoListUiSate.snackBarError) SnakeBarError(
//                    snackbarHostState = snackbarHostState,
//                    repoListUiSate = repoListUiSate,
//                    onRefreshButtonClicked = onRefreshButtonClicked
//                )
//
//
//                LazyColumn(
//                    Modifier
//                        .padding(innerPadding)
//                        .padding(horizontal = 16.dp)
//                        .padding(bottom = 16.dp)
//                ) {
//                    items(repoListUiSate.repoList) { githubRepoUiModel ->
//                        RepoItem(
//                            githubRepoUiModel = githubRepoUiModel,
//                            onRepoItemClicked = onRepoItemClicked
//                        )
//                    }
//                }
//            }
//        }
//
//    }
    }
}