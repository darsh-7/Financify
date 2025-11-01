package com.financify.presentation.screens.cam_scan_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financify.presentation.screens.cam_scan_screen.model.RepoIssuesUiState

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class RepoIssuesViewModel (
) : ViewModel() {
    private val _repoIssuesStateFlow = MutableStateFlow<RepoIssuesUiState>(RepoIssuesUiState(isLoading = true))
    val repoIssuesStateFlow: StateFlow<RepoIssuesUiState> = _repoIssuesStateFlow.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        _repoIssuesStateFlow.value = RepoIssuesUiState(
//            isLoading = false,
//            isError = true,
//            customRemoteExceptionUiModel = (throwable as CustomRemoteExceptionDomainModel).toCustomExceptionRemoteUiModel()
//        )
    }

    fun requestRepoIssues(
        ownerName: String, name: String
    ) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            /*try {
                val repoIssues = fetchReposIssuesUseCase(ownerName = ownerName, name = name)
                Log.i("IssuesViewModel", "repoIssues done with  ${_repoIssuesStateFlow.value.toString()}.")
//                _repoIssuesStateFlow.value = RepoIssuesUiState(
//                    isLoading = false,
//                    repoIssues = repoIssues.map { it.toRepoIssuesUiModel() }
//                )
            } catch (e: Exception) {
                Log.e("IssuesViewModel", "requestRepoIssues Exception : ${e.message}")

                _repoIssuesStateFlow.value = RepoIssuesUiState(
                    isLoading = false,
                    isError = true,
                    customRemoteExceptionUiModel = (e as CustomRemoteExceptionDomainModel).toCustomExceptionRemoteUiModel()
                )
            }*/
        }
    }
}