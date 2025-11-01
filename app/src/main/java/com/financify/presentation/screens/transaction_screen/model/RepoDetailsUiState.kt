package com.financify.presentation.screens.transaction_screen.model

import com.financify.presentation.model.CustomRemoteExceptionUiModel


//data class RepositoryDetailsUiState(
//    val repositoryDetails: RepositoryDetailsUiModel? = null,
//    val isLoading: Boolean = false,
//    val isError: Boolean = false,
//    val customErrorExceptionUiModel: CustomExceptionUiModel = CustomExceptionUiModel.Unknown
//)

sealed class RepoDetailsUiState {
    data object InitialState: RepoDetailsUiState()
    data class RepoDetailsUiModelData(val repositoryDetails: RepoDetailsUiModel) : RepoDetailsUiState()
    data class Loading(val isLoading: Boolean = true) : RepoDetailsUiState()
    data class Error(val customErrorExceptionUiModel: CustomRemoteExceptionUiModel) : RepoDetailsUiState()
    //data class Error(val errorMessage:String) : RepoDetailsUiState()
}