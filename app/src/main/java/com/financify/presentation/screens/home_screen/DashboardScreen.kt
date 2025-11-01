package com.financify.presentation.screens.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.githubreposapp.presentation.common_components.shimmer.issues.AnimateShimmerIssuesList
import com.financify.R
import com.financify.presentation.common_component.AppBar
import com.financify.presentation.common_component.ErrorSection
import com.financify.presentation.screens.cam_scan_screen.model.RepoIssuesUiState


@Composable
fun IssuesListScreen(
    owner: String,
    name: String,
    onClickBack: () -> Unit,
) {
//
//    val repoIssuesViewModel: RepoIssuesViewModel = hiltViewModel()
//
//    LaunchedEffect(Unit) {
//        repoIssuesViewModel.requestRepoIssues(ownerName = owner, name = name)
//    }
//
//    val repoIssuesUiState by repoIssuesViewModel.repoIssuesStateFlow.collectAsStateWithLifecycle()
//
//    IssuesListContent(
//        repoIssuesUiState = repoIssuesUiState,
//        onRefreshButtonClicked = {
//            repoIssuesViewModel.requestRepoIssues(ownerName = owner, name = name)
//        },
//        onClickBack = onClickBack
//    )
//    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun IssuesListContent(
        repoIssuesUiState: RepoIssuesUiState,
        modifier: Modifier = Modifier,
        onRefreshButtonClicked: () -> Unit,
        onClickBack: () -> Unit,
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                AppBar(
                    onBackButtonClicked = onClickBack, title = R.string.show_issues
                )
            }) { innerPadding ->

            when {
                repoIssuesUiState.isLoading -> {
                    AnimateShimmerIssuesList(
                        innerPadding = innerPadding
                    )
                }

                repoIssuesUiState.isError -> {
                    ErrorSection(
                        innerPadding = innerPadding,
                        customErrorExceptionUiModel = repoIssuesUiState.customRemoteExceptionUiModel,
                        onRefreshButtonClicked = {
                            onRefreshButtonClicked()
                        })
                }

                repoIssuesUiState.repoIssues.isNotEmpty() -> {
                    LazyColumn(
                        Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    ) {
//                        items(repoIssuesUiState.repoIssues) { repoItem ->
//                            IssuesItem(repoIssuesUiModel = repoItem)
//                        }
                    }
                }
            }

        }
    }
}