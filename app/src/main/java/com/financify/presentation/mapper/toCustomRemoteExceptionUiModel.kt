//package com.financify.presentation.mapper
//
//import android.util.Log
//import com.financify.domain.model.CustomRemoteExceptionDomainModel
//import com.financify.presentation.model.CustomRemoteExceptionUiModel
//
//
//fun CustomRemoteExceptionDomainModel.toCustomExceptionRemoteUiModel(): CustomRemoteExceptionUiModel {
//    Log.d("Exception", this.toString())
//    return when (this) {
//        is CustomRemoteExceptionDomainModel.NoInternetConnectionRemoteException -> CustomRemoteExceptionUiModel.NoInternetConnection
//        is CustomRemoteExceptionDomainModel.TimeOutExceptionRemoteException -> CustomRemoteExceptionUiModel.Timeout
//        is CustomRemoteExceptionDomainModel.ServiceUnavailableRemoteException -> CustomRemoteExceptionUiModel.ServiceUnreachable
//        is CustomRemoteExceptionDomainModel.AccessDeniedRemoteException -> CustomRemoteExceptionUiModel.ServiceUnreachable
//        is CustomRemoteExceptionDomainModel.ServiceNotFoundRemoteException -> CustomRemoteExceptionUiModel.ServiceUnreachable
//        is CustomRemoteExceptionDomainModel.ServiceNotFoundLocalException -> CustomRemoteExceptionUiModel.NoInternetConnection
//        is CustomRemoteExceptionDomainModel.UnknownRemoteException -> CustomRemoteExceptionUiModel.Unknown
//        else -> {
//            CustomRemoteExceptionUiModel.Unknown
//        }
//    }
//}