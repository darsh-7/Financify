package com.financify.presentation.screens.cam_scan_screen.model

import com.financify.presentation.model.CustomRemoteExceptionUiModel


data class RepoIssuesUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val customRemoteExceptionUiModel: CustomRemoteExceptionUiModel = CustomRemoteExceptionUiModel.Unknown,
    val repoIssues: List<RepoIssuesUiModel> = emptyList()
)
