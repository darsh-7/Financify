package com.financify.presentation.screens.cam_scan_screen

import androidx.compose.foundation.layout.fillMaxSize
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
import com.financify.R
import com.financify.data.data_sources.local.room.entities.TransactionType
import de.charlex.compose.SpeedDialData
import de.charlex.compose.SpeedDialFloatingActionButton

@Composable
fun RepoListScreen(
    onRepoItemClicked: (String, String) -> Unit
) {


}
/*
            --- TO BE MOVED TO THE HOME SCREEN ---
       ------- Just used it to test the App flow -------
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TransactionListScreen(
    onAddTransactionClicked: (TransactionType) -> Unit, modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = { Text("Financify Home") })
        },
        /*
                --- Speed Dial FLOATING ACTION BUTTON (FAB) ---
            TEMP: Just used it to test the App flow
            This is a third party library, LINK : https://github.com/ch4rl3x/SpeedDialFloatingActionButton
            didn't find a built-in one provides that speed dail
         */
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
        }) { paddingValues -> }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListContent(

) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(

    ) { innerPadding ->
        //   when {
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
//            repoListUiSate.repoList.isNotEmpty() -> {
//                if (repoListUiSate.snackBarError) SnakeBarError(
//                    snackbarHostState = snackbarHostState,
//                    repoListUiSate = repoListUiSate,
//                    onRefreshButtonClicked = onRefreshButtonClicked
//                )


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
    }
}
 */


//@Composable
//private fun SnakeBarError(
//
//) {
//
//}

@Preview(showSystemUi = true)
@Composable
private fun TempViewer() {
    MaterialTheme {
        TransactionListScreen(onAddTransactionClicked = {})
    }
}



