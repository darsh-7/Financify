package com.financify.presentation.screens.savings_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.financify.presentation.mapper.toCustomExceptionRemoteUiModel
//import com.financify.presentation.mapper.toGithubReposUiModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class RepoListViewModel (
//    private val githubReposListUseCase: FetchGithubReposListUseCase,
//    private val githubReposListCacheUseCase: FetchGithubReposListCacheUseCase

) : ViewModel() {
  //  private val _repoListStateFlow =
//        MutableStateFlow<RepoListUiState>(RepoListUiState(isLoading = true))
//    val repoListStateFlow: StateFlow<RepoListUiState> = _repoListStateFlow.asStateFlow()

//    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        _repoListStateFlow.value = RepoListUiState(
//            isLoading = false,
//            isError = true,
//            customRemoteExceptionUiModel = (throwable as CustomRemoteExceptionDomainModel).toCustomExceptionRemoteUiModel()
//        )
//    }

    fun requestGithubRepoList() {
//        _repoListStateFlow.value = RepoListUiState(
//            isLoading = true,
//        )
//        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
////            try {
////                val repoList = githubReposListUseCase()
////                _repoListStateFlow.value = RepoListUiState(
////                    isLoading = false,
////                    repoList = repoList.map { it.toGithubReposUiModel() }
////                )
////            } catch (e: CustomRemoteExceptionDomainModel) {
////                try {
////                    val repoList = githubReposListCacheUseCase()
////                    _repoListStateFlow.value = RepoListUiState(
////                        isLoading = false,
////                        isError = false,
////                        snackBarError = true,
////                        repoList = repoList.map { it.toGithubReposUiModel() },
////                        customRemoteExceptionUiModel = e.toCustomExceptionRemoteUiModel()
////
////                    )
////                } catch (e: CustomRemoteExceptionDomainModel) {
////                    _repoListStateFlow.value = RepoListUiState(
////                        isLoading = false,
////                        isError = true,
////                        customRemoteExceptionUiModel = e.toCustomExceptionRemoteUiModel()
////                    )
////                }
////            }
//        }
    }
}